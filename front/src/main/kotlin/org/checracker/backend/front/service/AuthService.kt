package org.checracker.backend.front.service

import org.checracker.backend.core.enum.Provider
import org.checracker.backend.core.repository.user.UserRepository
import org.checracker.backend.front.config.auth.JwtTokenProvider
import org.checracker.backend.front.model.request.LocalAuthRequest
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
    private val jwtTokenProvider: JwtTokenProvider,
    private val encoder: PasswordEncoder,
) {
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    fun signUpLocal(request: LocalAuthRequest): UserResponse {
        userRepository.findByEmailAndProvider(
            email = request.email,
            provider = Provider.CHECRACKER.name,
        )?.let {
            IllegalArgumentException("이미 사용중인 아이디입니다.")
        }

        val user = userRepository.save(request.toUserEntity(encoder))
        val token = jwtTokenProvider.createJwt(id = user.id!!, email = user.email, name = user.name)

        return user.toUserResponse(token)
    }

    @Transactional
    fun signInLocal(request: LocalAuthRequest): UserResponse {
        val user = userRepository.findByEmailAndProvider(
            email = request.email,
            provider = Provider.CHECRACKER.name,
        ) ?: throw AuthException("존재하지 않는 회원입니다. 회원 가입을 진행해주세요.")

        val token = if (encoder.matches(request.password, user.getPassword())) {
            user.checkDeletedUser() // 탈퇴 회원인지 체크
            jwtTokenProvider.createJwt(id = user.id!!, email = user.email, name = user.name)
        } else {
            throw AuthException("비밀번호가 일치하지 않습니다.")
        }

        return user.toUserResponse(token)
    }
}
