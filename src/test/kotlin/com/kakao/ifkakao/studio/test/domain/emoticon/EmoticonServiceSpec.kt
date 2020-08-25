package com.kakao.ifkakao.studio.test.domain.emoticon

import com.kakao.ifkakao.studio.domain.emoticon.EmoticonInformation
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonRepository
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonService
import com.kakao.ifkakao.studio.test.Mock
import com.kakao.ifkakao.studio.test.SpringDataConfig
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.single
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.stringPattern
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = [SpringDataConfig::class])
class EmoticonServiceSpec(
    emoticonRepository: EmoticonRepository
) : ExpectSpec() {
    private val emoticonService = EmoticonService(repository = emoticonRepository)

    init {
        context("create - 계정과 이모티콘 정보, 이미지로 이모티콘 생성을 하면") {
            val account = Mock.account(identified = true)
            val information = information()
            val images = images()
            expect("이모티콘이 생성된다.") {
                val emoticon = emoticonService.create(account, information, images)
                emoticon.id shouldBeGreaterThan 0
                emoticon.authorId shouldBe account.id
                emoticon.title shouldBe information.title
                emoticon.description shouldBe information.description
                emoticon.choco shouldBe information.choco
                emoticon.images shouldContainAll images
            }
        }
    }

    private fun information() = EmoticonInformation(
        title = Arb.string(10..100).single(),
        description = Arb.string(100..300).single(),
        choco = Arb.int(100..500).single()
    )

    private fun images() = Arb.stringPattern("([a-zA-Z0-9]{1,10})/([a-zA-Z0-9]{1,10})\\.jpg")
        .chunked(1..10)
        .single()
}