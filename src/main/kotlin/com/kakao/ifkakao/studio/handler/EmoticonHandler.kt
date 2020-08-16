package com.kakao.ifkakao.studio.handler

import com.kakao.ifkakao.studio.domain.account.AccountService
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonService
import com.kakao.ifkakao.studio.handler.request.RegisterEmoticon
import com.kakao.ifkakao.studio.handler.response.EmoticonRegistered

class EmoticonHandler(
    private val accountService: AccountService,
    private val emoticonService: EmoticonService
) {
    fun register(token: String, request: RegisterEmoticon): EmoticonRegistered {
        TODO()
    }
}
