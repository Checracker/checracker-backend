package org.checracker.backend.front.controller

import io.swagger.v3.oas.annotations.media.Schema
import org.checracker.backend.core.entity.user.User
import org.checracker.backend.front.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/user")
class UserController(
    private val userService: UserService,
) {
    @Schema(description = "테스트용")
    @GetMapping("/test")
    fun getUsers(): List<User> {
        println("LOGGING ::: TEST")
        return userService.getUsers()
    }
}
