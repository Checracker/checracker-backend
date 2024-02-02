package org.checracker.backend.front.model.request

import org.checracker.backend.core.entity.user.User
import org.checracker.backend.core.enum.Provider
import org.checracker.backend.front.util.UserUtil
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class LocalAuthRequest(
    @NotBlank
    @field:Email
    val email: String,

    @NotBlank
    @field:Size(min = 7, max = 25)
    val password: String,

    @NotBlank
    val name: String,
)

fun LocalAuthRequest.toUserEntity(encoder: PasswordEncoder) = User(
    provider = Provider.CHECRACKER.name,
    email = email,
    password = encoder.encode(password),
    name = name,
    nickname = UserUtil.generateRandomNickname(),
    profileImage = "", // TODO : 기본 이미지 주소
)
