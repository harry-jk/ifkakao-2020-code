package com.kakao.ifkakao.studio.domain.notification

import com.kakao.ifkakao.studio.domain.emoticon.Emoticon

interface EmailService {
    fun send(to: List<String>, body: List<Emoticon>)
}