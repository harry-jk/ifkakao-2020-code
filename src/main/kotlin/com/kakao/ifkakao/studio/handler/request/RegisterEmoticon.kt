package com.kakao.ifkakao.studio.handler.request

data class RegisterEmoticon(
    val title: String,
    val description: String,
    val choco: Int,
    val images: List<String>
)