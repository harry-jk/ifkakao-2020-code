package com.kakao.ifkakao.studio.handler

import com.kakao.ifkakao.studio.domain.account.AccountService
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonInformation
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonService
import com.kakao.ifkakao.studio.exception.AccountNotFoundException
import com.kakao.ifkakao.studio.handler.request.RegisterEmoticon
import com.kakao.ifkakao.studio.handler.response.EmoticonRegistered

class EmoticonHandler(
    private val accountService: AccountService,
    private val emoticonService: EmoticonService
) {
    fun register(token: String, request: RegisterEmoticon): EmoticonRegistered {
        val account = accountService.take(token) ?: throw AccountNotFoundException()
        val emoticon = emoticonService.create(
            account,
            EmoticonInformation(request.title, request.description, request.choco),
            request.images
        )
        return EmoticonRegistered(
            emoticonId = emoticon.id,
            authorId = emoticon.authorId,
            title = emoticon.title,
            description = emoticon.description,
            choco = emoticon.choco,
            images = emoticon.images
        )
    }
}
