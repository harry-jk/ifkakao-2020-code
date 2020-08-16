package com.kakao.ifkakao.studio.domain.emoticon

import com.kakao.ifkakao.studio.domain.account.AccountId

typealias EmoticonId = Long

data class Emoticon(
    val id: EmoticonId,
    val authorId: AccountId,
    val title: String,
    val description: String,
    val choco: Int,
    val images: List<String>
)

data class EmoticonInformation(
    val title: String,
    val description: String,
    val choco: Int
)