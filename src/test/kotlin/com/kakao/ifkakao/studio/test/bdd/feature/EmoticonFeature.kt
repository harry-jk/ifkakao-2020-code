package com.kakao.ifkakao.studio.test.bdd.feature

import com.kakao.ifkakao.studio.domain.account.AccountService
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonRepository
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonService
import com.kakao.ifkakao.studio.domain.notification.EmailService
import com.kakao.ifkakao.studio.handler.EmoticonHandler
import com.kakao.ifkakao.studio.test.Mock
import com.kakao.ifkakao.studio.test.SpringDataConfig
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.single
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
    private val emailService = mockk<EmailService>(relaxed = true)
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
            val KST = ZoneOffset.ofHours(9)
            beforeTest {
                every { accountService.getAdminList() } returns admins
                Mock.emoticonEntity(LocalDate.of(2020, 8, 1), KST).chunked(10..100).single()
                    .also { emoticonRepository.saveAll(it) }
                Mock.emoticonEntity(LocalDate.of(2020, 8, 2), KST).chunked(10..100).single()
                    .also { emoticonRepository.saveAll(it) }
                Mock.emoticonEntity(LocalDate.of(2020, 8, 3), KST).chunked(10..100).single()
                    .also { emoticonRepository.saveAll(it) }
            }

            scenario("2020-08-05 11:00:00(KST)에 전날 생성된 이모티콘 목록을 검수자에게 이메일 발송 되어야 한다.") {
                val targets = Mock.emoticonEntity(LocalDate.of(2020, 8, 4), KST)
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

}