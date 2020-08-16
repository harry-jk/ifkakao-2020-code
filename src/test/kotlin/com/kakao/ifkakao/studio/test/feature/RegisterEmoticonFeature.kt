package com.kakao.ifkakao.studio.test.feature

import com.kakao.ifkakao.studio.domain.account.Account
import com.kakao.ifkakao.studio.domain.account.AccountService
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonService
import com.kakao.ifkakao.studio.handler.EmoticonHandler
import com.kakao.ifkakao.studio.handler.request.RegisterEmoticon
import com.kakao.ifkakao.studio.test.Mock
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.single
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.stringPattern
import io.mockk.every
import io.mockk.mockk

class RegisterEmoticonFeature : BehaviorSpec() {
    private val accountService = mockk<AccountService>() // MSA
    private val emoticonService = EmoticonService()
    private val emoticonHandler = EmoticonHandler(
        accountService = accountService,
        emoticonService = emoticonService
    )

    init {
        Given("본인 인증된 사용자가 로그인된 상황에서") {
            val token = Arb.stringPattern("([a-zA-Z0-9]{20})").single()
            val account = Mock.account(identified = true)
            every { accountService.take(token) } returns account

            When("검수 정보를 입력하고 검수 등록 버튼을 누르면”") {
                val request = request()

                Then("검수 등록 완료화면으로 이동 되어야한다") {
                    val response = emoticonHandler.register(token, request)

                    response.authorId shouldBe account.id
                    response.title shouldBe request.title
                    response.description shouldBe request.description
                    response.choco shouldBe request.choco
                    response.images shouldContainAll request.images
                }
            }
        }
    }

    private fun request() = RegisterEmoticon(
        title = Arb.string(10..100).single(),
        description = Arb.string(100..300).single(),
        choco = Arb.int(100..500).single(),
        images = Arb.stringPattern("([a-zA-Z0-9]{1,10})/([a-zA-Z0-9]{1,10})\\.jpg")
            .chunked(1..10)
            .single()
    )
}