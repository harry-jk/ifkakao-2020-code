package com.kakao.ifkakao.studio.test.bdd.behavior

import com.kakao.ifkakao.studio.domain.account.AccountService
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonEntity
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonRepository
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonService
import com.kakao.ifkakao.studio.exception.DeniedRegisterEmoticonException
import com.kakao.ifkakao.studio.handler.EmoticonHandler
import com.kakao.ifkakao.studio.handler.request.RegisterEmoticon
import com.kakao.ifkakao.studio.test.Mock
import com.kakao.ifkakao.studio.test.SpringDataConfig
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.IsolationMode.InstancePerLeaf
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.single
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.stringPattern
import io.mockk.checkEquals
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = [SpringDataConfig::class])
class RegisterEmoticonFeature(
    _emoticonRepository: EmoticonRepository
) : BehaviorSpec() {
    private val emoticonRepository = spyk(_emoticonRepository)
    private val accountService = mockk<AccountService>() // MSA
    private val emoticonService = EmoticonService(repository = emoticonRepository)
    private val emoticonHandler = EmoticonHandler(
        accountService = accountService,
        emoticonService = emoticonService
    )

    override fun isolationMode(): IsolationMode = InstancePerLeaf

    init {
        Given("본인 인증된 사용자가 로그인된 상황에서") {
            val token = Arb.stringPattern("([a-zA-Z0-9]{20})").single()
            val account = Mock.account(identified = true)
            every { accountService.take(ofType(String::class)) } returns account

            When("검수 정보를 입력란에") {
                And("검수 정보를 입력하고 검수 등록 버튼을 누르면") {
                    val request = request()
                    val response = emoticonHandler.register(token, request)

                    Then("등록 결과가 포함된 검수 진행 목록 화면으로 이동한다") {
                        response.authorId shouldBe account.id
                        response.title shouldBe request.title
                        response.description shouldBe request.description
                        response.choco shouldBe request.choco
                        response.images shouldContainAll request.images

                        verify(exactly = 1) {
                            accountService.take(token = token)
                            emoticonRepository.save(match<EmoticonEntity> {
                                it.accountId == account.id
                            })
                        }
                    }
                }

                And("검수 정보를 입력하지 않고 검수 등록 버튼을 누르면") {
                    val ex = shouldThrowExactly<DeniedRegisterEmoticonException> {
                        emoticonHandler.register(token, null)
                    }

                    Then("검수 등록 실패 사유가 화면에 표시되어야 한다") {
                        ex.message.isNotBlank() shouldBe true

                        verify(exactly = 0) {
                            accountService.take(any())
                            emoticonRepository.save(ofType<EmoticonEntity>())
                        }
                    }
                }
            }
        }
    }

    private fun request() = RegisterEmoticon(
        title = Arb.string(10..100).single(),
        description = Arb.string(250..300).single(),
        choco = Arb.int(100..500).single(),
        images = Arb.stringPattern("([a-zA-Z0-9]{1,10})/([a-zA-Z0-9]{1,10})\\.jpg")
            .chunked(1..10)
            .single()
    )
}