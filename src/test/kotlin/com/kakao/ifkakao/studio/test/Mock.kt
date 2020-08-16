package com.kakao.ifkakao.studio.test

import com.kakao.ifkakao.studio.domain.account.Account
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.single
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.stringPattern

object Mock {
    fun account(identified: Boolean) = Account(
        id = Arb.long(min = 1).single(),
        name = Arb.string(5..20).single(),
        email = Arb.stringPattern("([a-zA-Z0-9]{5,20})\\@test\\.kakao\\.com").single(),
        identified = identified
    )
}