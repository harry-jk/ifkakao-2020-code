package com.kakao.ifkakao.studio.test.bdd.feature

import com.kakao.ifkakao.studio.domain.account.Account
import com.kakao.ifkakao.studio.domain.account.AccountService
import com.kakao.ifkakao.studio.domain.emoticon.Emoticon
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonEntity
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonRepository
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonService
import com.kakao.ifkakao.studio.domain.notification.EmailService
import com.kakao.ifkakao.studio.handler.EmoticonHandler
import com.kakao.ifkakao.studio.test.Mock
import com.kakao.ifkakao.studio.test.SpringDataConfig
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arb
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.instant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.single
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.stringPattern
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

@ContextConfiguration(classes = [SpringDataConfig::class])
class EmoticonFeature(
    private val emoticonRepository: EmoticonRepository
) : FeatureSpec() {
    private val accountService = mockk<AccountService>() // MSA
    private val emoticonService = EmoticonService(repository = emoticonRepository)
    private val emailService = mockk<EmailService>()
    private val emoticonHandler = EmoticonHandler(
        accountService = accountService,
        emoticonService = emoticonService,
        emailService = emailService
    )

    private val admins = listOf(
        Mock.account(true),
        Mock.account(true),
        Mock.account(true)
    )

    init {
        feature("이모티콘 검수 이메일 발송") {
            beforeTest {
                every { accountService.getAdminList() } returns admins
                emoticon(LocalDate.of(2020, 8, 1)).chunked(10..100).single()
                    .also { emoticonRepository.saveAll(it) }
                emoticon(LocalDate.of(2020, 8, 2)).chunked(10..100).single()
                    .also { emoticonRepository.saveAll(it) }
                emoticon(LocalDate.of(2020, 8, 3)).chunked(10..100).single()
                    .also { emoticonRepository.saveAll(it) }
            }
            scenario("2020-08-05 11:00:00(KST)에 전날 생성된 이모티콘 목록을 검수자에게 이메일 발송 되어야 한다.") {
                val targets = emoticon(LocalDate.of(2020, 8, 4))
                    .chunked(10..100)
                    .single()
                    .let { emoticonRepository.saveAll(it) }
                    .map { it.id }

                val now = ZonedDateTime.of(
                    LocalDate.of(2020, 8, 5),
                    LocalTime.of(11, 0, 0),
                    ZoneOffset.ofHours(9) // KST
                )

                emoticonHandler.report(now)

                verify(exactly = 1) {
                    emailService.send(
                        match { it.containsAll(admins.map { admin -> admin.email }) },
                        match {
                            it.count() == targets.count()
                                && it.map { emoticon -> emoticon.id }.containsAll(targets)
                        }
                    )
                }
            }
        }
    }

    private fun emoticon(createdDate: LocalDate) = arb { rs ->
        val accountId = Arb.long(100_000L..200_000L)
        val title = Arb.string(10..100)
        val description = Arb.string(100..300)
        val choco = Arb.int(100..500)
        val images = Arb.stringPattern("([a-zA-Z0-9]{1,10})/([a-zA-Z0-9]{1,10})\\.jpg")
            .chunked(1..10)
        val created = Arb.instant(
            minValue = createdDate.atTime(LocalTime.MIN).atZone(ZoneOffset.ofHours(9)).toInstant(),
            maxValue = createdDate.atTime(LocalTime.MAX).atZone(ZoneOffset.ofHours(9)).toInstant()
        )

        generateSequence {
            created.next(rs).let { createdAt ->
                EmoticonEntity(
                    accountId = accountId.next(rs),
                    title = title.next(rs),
                    description = description.next(rs),
                    choco = choco.next(rs),
                    images = images.next(rs),
                    createdAt = createdAt,
                    updatedAt = createdAt
                )
            }
        }
    }
}