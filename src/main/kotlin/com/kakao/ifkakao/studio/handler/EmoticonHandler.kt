package com.kakao.ifkakao.studio.handler

import com.kakao.ifkakao.studio.domain.account.AccountService
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonInformation
import com.kakao.ifkakao.studio.domain.emoticon.EmoticonService
import com.kakao.ifkakao.studio.domain.notification.EmailService
import com.kakao.ifkakao.studio.exception.AccountNotFoundException
import com.kakao.ifkakao.studio.exception.DeniedRegisterEmoticonException
import com.kakao.ifkakao.studio.handler.request.RegisterEmoticon
import com.kakao.ifkakao.studio.handler.response.EmoticonRegistered
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class EmoticonHandler(
    private val accountService: AccountService,
    private val emoticonService: EmoticonService,
    private val emailService: EmailService
) {
    fun register(token: String, request: RegisterEmoticon?): EmoticonRegistered {
        if (request == null) throw DeniedRegisterEmoticonException("이모티콘 검수 정보를 입력해주세요.")
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

    fun report(now: ZonedDateTime) {
        val adminEmail = accountService.getAdminList().map { it.email }
        val targetDate = now.withZoneSameInstant(ZoneOffset.ofHours(9))
            .minusDays(1L)
            .truncatedTo(ChronoUnit.DAYS)

        val emoticons = emoticonService.getAllCreatedAt(
            from = targetDate,
            to = targetDate.plusDays(1).minusNanos(1)
        )

        emailService.send(adminEmail, emoticons)
    }
}
