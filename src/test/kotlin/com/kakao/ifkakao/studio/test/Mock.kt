package com.kakao.ifkakao.studio.test

import com.kakao.ifkakao.studio.domain.account.Account
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonEntity
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
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

object Mock {
    fun account(identified: Boolean) = Account(
        id = Arb.long(min = 1).single(),
        name = Arb.string(5..20).single(),
        email = Arb.stringPattern("([a-zA-Z0-9]{5,20})\\@test\\.kakao\\.com").single(),
        identified = identified
    )


    fun emoticonEntity(createdDate: LocalDate, zone: ZoneId) = arb { rs ->
        val accountId = Arb.long(100_000L..200_000L)
        val title = Arb.string(10..100)
        val description = Arb.string(100..300)
        val choco = Arb.int(100..500)
        val images = Arb.stringPattern("([a-zA-Z0-9]{1,10})/([a-zA-Z0-9]{1,10})\\.jpg")
            .chunked(1..10)
        val created = Arb.instant(
            minValue = createdDate.atTime(LocalTime.MIN).atZone(zone).toInstant(),
            maxValue = createdDate.atTime(LocalTime.MAX).atZone(zone).toInstant()
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