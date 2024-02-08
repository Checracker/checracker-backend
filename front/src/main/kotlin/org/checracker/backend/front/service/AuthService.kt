package org.checracker.backend.front.service

import org.checracker.backend.core.enum.Provider
import org.checracker.backend.core.repository.user.UserRefreshTokenRepository
import org.checracker.backend.core.repository.user.UserRepository
import org.checracker.backend.front.config.auth.JwtTokenProvider
import org.checracker.backend.front.model.dto.JwtDto
import org.checracker.backend.front.model.request.LocalAuthSignInRequest
import org.checracker.backend.front.model.request.LocalAuthSignUpRequest
import org.checracker.backend.front.model.request.toUserEntity
import org.checracker.backend.front.model.response.UserResponse
import org.checracker.backend.front.model.response.toUserResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import javax.security.auth.message.AuthException

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val userRefreshTokenRepository: UserRefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val encoder: PasswordEncoder,
) {
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    fun signUpLocal(request: LocalAuthSignUpRequest): UserResponse {
        userRepository.findByEmailAndProvider(
            email = request.email,
            provider = Provider.CHECRACKER.name,
        )?.let {
            throw IllegalArgumentException("이미 사용중인 아이디입니다.")
        }

        val user = userRepository.save(request.toUserEntity(encoder))
        val token = jwtTokenProvider.createJwt(id = user.id!!, email = user.email, name = user.name)

        return user.toUserResponse(token)
    }

    @Transactional
    fun signInLocal(request: LocalAuthSignInRequest): UserResponse {
        val user = userRepository.findByEmailAndProvider(
            email = request.email,
            provider = Provider.CHECRACKER.name,
        ) ?: throw AuthException("존재하지 않는 회원입니다. 회원 가입을 진행해주세요.")

        val token = if (encoder.matches(request.password, user.password)) {
            user.checkDeletedUser() // 탈퇴 회원인지 체크
            jwtTokenProvider.createJwt(id = user.id!!, email = user.email, name = user.name)
        } else {
            throw AuthException("비밀번호가 일치하지 않습니다.")
        }

        return user.toUserResponse(token)
    }

    @Transactional
    fun refresh(refreshToken: String): JwtDto {
        jwtTokenProvider.validateRefreshToken(refreshToken)

        // refreshToken이 저장되어있는지 확인
        val userRefreshToken = userRefreshTokenRepository.findByRefreshToken(refreshToken) ?: throw AuthException(
            "존재하지 않는 refreshToken입니다.",
        )

        // 유저 찾기 + 탈퇴한 회원인지
        val user = userRepository.findUserById(id = userRefreshToken.userId)
            ?: throw AuthException("존재하지 않는 유저라서 token 갱신에 실패했습니다.")
        user.checkDeletedUser()

        // token 새로 발급받기
        val token = jwtTokenProvider.createJwt(
            id = userRefreshToken.userId,
            email = user.email,
            name = user.name,
        ) // toekn 새로 만들기
        userRefreshToken.updateRefreshToken(token.refreshToken.token) // 새로 발급받은 refreshToken을 저장

        return token
    }
}
