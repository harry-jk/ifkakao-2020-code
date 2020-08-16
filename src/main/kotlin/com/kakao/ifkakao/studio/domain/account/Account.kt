package com.kakao.ifkakao.studio.domain.account

typealias AccountId = Long

data class Account(
    val id: AccountId,
    val name: String,
    val email: String,
    val identified: Boolean
)
