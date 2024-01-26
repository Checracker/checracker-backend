package org.checracker.backend.front.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.checracker.backend.front.model.request.LocalAuthRequest
import org.checracker.backend.front.model.response.UserResponse
import org.checracker.backend.front.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "인증 API", description = "인증 관련 API")
@RestController
@RequestMapping("v1/auth")
class AuthController(
    private val authService: AuthService,
) {

    @Operation(summary = "로컬 회원가입", description = "로컬 회원가입을 합니다.")
    @PostMapping("/sign-up/local")
    fun signUpLocal(
        @RequestBody request: LocalAuthRequest,
    ): UserResponse {
        return authService.signUpLocal(request = request)
    }

    @Operation(summary = "로컬 로그인", description = "로컬 로그인을 합니다.")
    @PostMapping("/sign-in/local")
    fun signInLocal(
        @RequestBody request: LocalAuthRequest,
    ): UserResponse {
        return authService.signInLocal(request = request)
    }
}
