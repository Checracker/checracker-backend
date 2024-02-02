package org.checracker.backend.core.repository.user

import org.checracker.backend.core.entity.user.UserRefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface UserRefreshTokenRepository : JpaRepository<UserRefreshToken, Long> {
    fun findByUserId(userId: Long): UserRefreshToken?
    fun findByRefreshToken(refreshToken: String): UserRefreshToken?
}
