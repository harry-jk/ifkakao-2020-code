package com.kakao.ifkakao.studio.domain.emoticon

import org.springframework.data.repository.CrudRepository
import java.time.Instant
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "emoticon")
data class EmoticonEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = 0,
    val accountId: Long,
    val title: String,
    @Column(length = 300) val description: String,
    val choco: Int,
    @ElementCollection val images: List<String>,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = createdAt
)

interface EmoticonRepository : CrudRepository<EmoticonEntity, Long> {
    fun findAllByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(from: Instant, to: Instant): List<EmoticonEntity>
}
