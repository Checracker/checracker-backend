package org.checracker.backend.front.model.request

import org.checracker.backend.core.entity.user.User
import org.checracker.backend.core.enum.Provider
import org.checracker.backend.front.util.UserUtil
import org.springframework.security.crypto.password.PasswordEncoder

data class LocalAuthRequest(
    val email: String,
    val password: String,
    val name: String,
) // TODO : validation 걸기

fun LocalAuthRequest.toUserEntity(encoder: PasswordEncoder) = User(
    provider = Provider.CHECRACKER.name,
    email = email,
    password = encoder.encode(password),
    name = name,
    nickname = UserUtil.generateRandomNickname(),
    profileImage = "", // TODO : 기본 이미지 주소
)
