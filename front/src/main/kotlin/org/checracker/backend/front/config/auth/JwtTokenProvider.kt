package org.checracker.backend.front.config.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.checracker.backend.core.common.Logger
import org.checracker.backend.core.entity.user.UserRefreshToken
import org.checracker.backend.core.repository.user.UserRefreshTokenRepository
import org.checracker.backend.core.repository.user.UserRepository
import org.checracker.backend.front.model.dto.JwtDto
import org.checracker.backend.front.model.dto.TokenDto
import org.checracker.backend.front.model.toLoginUser
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.SecretKey
import javax.security.auth.message.AuthException
import javax.servlet.http.HttpServletRequest

@Component
@ConfigurationProperties(prefix = "jwt")
class JwtTokenProvider(
    private val userRepository: UserRepository,
    private val userRefreshTokenRepository: UserRefreshTokenRepository,
) {
    lateinit var tokenSecretKey: String
    lateinit var accessTokenValidTime: String
    lateinit var refreshTokenValidTime: String
    lateinit var key: SecretKey

    companion object : Logger() {
        private const val BEARER_TYPE = "Bearer "
        private const val AUTHORITIES_KEY = "authorities"
    }

    @PostConstruct
    fun init() {
        tokenSecretKey = Base64.getEncoder().encodeToString(tokenSecretKey.toByteArray())

        val keyBytes = Decoders.BASE64.decode(tokenSecretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createToken(id: Long, name: String, expireDate: Date?): String {
        return Jwts.builder()
            .setId(id.toString())
            .setSubject(name)
            .setExpiration(expireDate)
            .signWith(key)
            .compact()
    }

    fun createJwt(id: Long, email: String, name: String): JwtDto {
        val now = System.currentTimeMillis()
        val nowLocalDateTime = toLocalDateTime(Instant.ofEpochMilli(now))

        val accessTokenExpireTime = Date(now + accessTokenValidTime.toLong())
        val refreshTokenExpireTime = Date(now + refreshTokenValidTime.toLong())

        val accessToken = createToken(id, name, accessTokenExpireTime)
        val refreshToken = createToken(id, name, refreshTokenExpireTime)

        // refreshToken 있으면 업데이트하고, 없으면 save
        userRefreshTokenRepository.findByUserId(id)?.updateRefreshToken(refreshToken) ?: run {
            userRefreshTokenRepository.save(
                UserRefreshToken(
                    userId = id,
                    refreshToken = refreshToken,
                ),
            )
        }

        return JwtDto(
            accessToken = TokenDto(
                token = accessToken,
                createdAt = nowLocalDateTime,
                expiredAt = toLocalDateTime(accessTokenExpireTime.toInstant()),
            ),
            refreshToken = TokenDto(
                token = refreshToken,
                createdAt = nowLocalDateTime,
                expiredAt = toLocalDateTime(refreshTokenExpireTime.toInstant()),

            ),
            email = email,
            name = name,
        )
    }

    // Request Header에서 토큰 정보를 꺼내오기 위한 메소드
    fun resolveToken(request: HttpServletRequest): String {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7)
        }

        return ""
    }

    fun validateAccessToken(token: String): Boolean {
        return runCatching {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        }.onFailure {
            logger.warn("accessToken 만료 또는 잘못된 형식입니다.")
        }.getOrElse { false }
    }

    fun validateRefreshToken(token: String) {
        runCatching {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
        }.onFailure {
            throw AuthException("refreshToken 만료 또는 잘못된 형식으로 로그인이 필요합니다.")
        }
    }

    fun getAuthentication(token: String): Authentication {
        val claims: Claims = Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        val userId = claims.id.toLong()
        val userDetails = userRepository.findByIdOrNull(userId)?.toLoginUser()
            ?: throw UsernameNotFoundException(userId.toString())

        return UsernamePasswordAuthenticationToken(userDetails, token, listOf())
    }

    private fun toLocalDateTime(instant: Instant) = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
}
