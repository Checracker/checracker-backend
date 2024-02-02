package org.checracker.backend.core.entity.user

import org.checracker.backend.core.common.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "user_refresh_token")
@Entity
data class UserRefreshToken(
    @Id
    @GeneratedValue
    val id: Long? = null,
    val userId: Long,
    @Column(columnDefinition = "TEXT")
    var refreshToken: String,
) : BaseEntity(){
    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
        this.updatedAt = LocalDateTime.now()
    }
}
