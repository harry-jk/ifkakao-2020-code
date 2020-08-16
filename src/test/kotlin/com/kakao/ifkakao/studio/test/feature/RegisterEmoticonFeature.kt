package com.kakao.ifkakao.studio.test.feature

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.single
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
            val account = account(identified = true)
            every { accountService.take(token) } returns account

            When("검수 정보를 입력하고 검수 등록 버튼을 누르면”") {
                val request = request()
                val response = emoticonHandler.register(token, request)

                Then("검수 등록 완료화면으로 이동 되어야한다") {
                    response.authorId shouldBe account.id
                    response.title shouldBe request.title
                    response.description shouldBe request.description
                    response.choco shouldBe request.choco
                    response.images shouldContainAll request.images
                }
            }
        }
    }
}