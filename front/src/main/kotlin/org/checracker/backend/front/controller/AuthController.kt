package org.checracker.backend.front.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.checracker.backend.front.model.dto.JwtDto
import org.checracker.backend.front.model.request.LocalAuthSignInRequest
import org.checracker.backend.front.model.request.LocalAuthSignUpRequest
import org.checracker.backend.front.model.request.UserRefreshTokenRequest
import org.checracker.backend.front.model.response.UserResponse
import org.checracker.backend.front.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Tag(name = "인증 API", description = "인증 관련 API")
@RestController
@RequestMapping("v1/auth")
class AuthController(
    private val authService: AuthService,
) {

    @Operation(summary = "로컬 회원가입", description = "로컬 회원가입을 합니다.")
    @PostMapping("/sign-up/local")
    fun signUpLocal(
        @Valid @RequestBody
        request: LocalAuthSignUpRequest,
    ): UserResponse {
        return authService.signUpLocal(request = request)
    }

    @Operation(summary = "로컬 로그인", description = "로컬 로그인을 합니다.")
    @PostMapping("/sign-in/local")
    fun signInLocal(
        @Valid @RequestBody
        request: LocalAuthSignInRequest,
    ): UserResponse {
        return authService.signInLocal(request = request)
    }

    @Operation(summary = "accessToken 재발급", description = "refreshToken 토큰으로 accessToken, refreshToken 재발급")
    @PostMapping("/refresh")
    fun refresh(@RequestBody request: UserRefreshTokenRequest): JwtDto {
        return authService.refresh(request.refreshToken)
    }
}
