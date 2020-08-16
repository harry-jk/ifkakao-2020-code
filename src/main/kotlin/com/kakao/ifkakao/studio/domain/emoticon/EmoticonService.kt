package com.kakao.ifkakao.studio.domain.emoticon

import com.kakao.ifkakao.studio.domain.account.Account

class EmoticonService(
    private val repository: EmoticonRepository
) {
    fun create(account: Account, information: EmoticonInformation, images: List<String>): Emoticon =
        repository.save(EmoticonEntity(
            accountId = account.id,
            title = information.title,
            description = information.description,
            choco = information.choco,
            images = images
        )).dto()

    private fun EmoticonEntity.dto() = Emoticon(
        id = id,
        authorId = accountId,
        title = title,
        description = description,
        choco = choco,
        images = images
    )
}
