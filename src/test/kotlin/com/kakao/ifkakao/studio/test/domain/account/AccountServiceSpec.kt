package com.kakao.ifkakao.studio.test.domain.account

import com.kakao.ifkakao.studio.domain.account.AccountService
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class AccountServiceSpec : ExpectSpec() {
    private val accountService = AccountService()

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
}