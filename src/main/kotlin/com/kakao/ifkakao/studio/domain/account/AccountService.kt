package com.kakao.ifkakao.studio.domain.account

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

class AccountService(apiUrl: String) {
    private val client = WebClient.create(apiUrl)

    fun take(token: String): Account? = client
        .get()
        .uri("/api/v1/account")
        .header("Authorization", "Bear $token")
        .retrieve()
        .bodyToMono(Account::class.java)
        .onErrorResume {
            when(it) {
                is WebClientResponseException.NotFound -> Mono.empty()
                else -> Mono.error(it)
            }
        }
        .block()

}
