package com.kakao.ifkakao.studio.handler.response

import com.kakao.ifkakao.studio.domain.account.AccountId
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonId

data class EmoticonRegistered(
    val emoticonId: EmoticonId,
    val authorId: AccountId,
    val title: String,
    val description: String,
    val choco: Int,
    val images: List<String>
)
