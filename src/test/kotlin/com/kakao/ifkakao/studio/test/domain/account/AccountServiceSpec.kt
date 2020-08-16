package com.kakao.ifkakao.studio.test.domain.account

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kakao.ifkakao.studio.domain.account.AccountService
import com.kakao.ifkakao.studio.test.Mock
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.single
import org.mockserver.client.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.Not
import kotlin.random.Random

class AccountServiceSpec : ExpectSpec() {
    private val apiMock: MockServerClient =
        ClientAndServer.startClientAndServer(Arb.int(49152..65500).single())
    private val accountService = AccountService("http://localhost:${apiMock.port}")

    override fun beforeSpec(spec: Spec) {
       setupStub()
    }

    override fun afterSpec(spec: Spec) {
        apiMock.close()
    }

    init {
        context("take - token으로 계정을 조회하면") {
            context("계정이 있는경우") {
                expect("Account가 반환된다.") {
                    val result = accountService.take("token")
                    result shouldNotBe null
                }
            }
            context("계정이 없는경우") {
                expect("null이 반환된다.") {
                    val result = accountService.take("not-found-token")
                    result shouldBe null
                }
            }
        }
    }

    private fun setupStub() {
        apiMock.`when`(
            request()
                .withMethod("GET")
                .withPath("/api/v1/account")
                .withHeader("Authorization", "Bear token")
        ).respond(
            response()
                .withHeader("Content-Type", "application/json")
                .withBody(jacksonObjectMapper().writeValueAsString(Mock.account(true)))
        )

        apiMock.`when`(
            request()
                .withMethod("GET")
                .withPath("/api/v1/account")
                .withHeader("Authorization", "Bear not-found-token")
        ).respond(
            response().withStatusCode(404)
        )
    }
}