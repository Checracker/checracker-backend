package org.checracker.backend.front.service

import org.checracker.backend.core.enum.Provider
import org.checracker.backend.core.repository.user.UserRepository
import org.checracker.backend.front.model.request.LocalAuthRequest
import org.checracker.backend.front.model.request.toUserEntity
import org.springframework.stereotype.Service
import javax.security.auth.message.AuthException

@Service
class AuthService(
    private val userRepository: UserRepository,
) {
    fun localLogin(request: LocalAuthRequest) {
        userRepository.findByEmailAndProvider(
            email = request.email,
            provider = Provider.CHECRACKER.name,
        )?.let {
            if (it.isDeleted && it.deletedAt != null) {
                throw AuthException("탈퇴 처리가 된 유저입니다.")
            } else {
                it.checkPassword(password = request.password)
            }
        } ?: kotlin.run { // 없으면 자동 회원가입
            userRepository.save(
                request.toUserEntity()
            )
        }
    }
}
