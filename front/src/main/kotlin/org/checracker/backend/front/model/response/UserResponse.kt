package org.checracker.backend.front.model.response

import org.checracker.backend.core.entity.user.User
import org.checracker.backend.front.model.dto.JwtDto
import org.checracker.backend.front.model.dto.TokenDto

data class UserResponse(
    val email: String,
    val name: String,
    val nickname: String,
    val profileImage: String,
    val accessToken: TokenDto,
    val refreshToken: TokenDto,
)

fun User.toUserResponse(token: JwtDto) = UserResponse(
    accessToken = token.accessToken,
    refreshToken = token.refreshToken,
    email = email,
    name = name,
    nickname = nickname ?: "닉네임을 설정해주세요",
    profileImage = profileImage,
)
