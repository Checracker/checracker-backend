package org.checracker.backend.front.controller

import org.checracker.backend.core.user.entity.User
import org.checracker.backend.front.model.request.UserRequest
import org.checracker.backend.front.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/user")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/test")
    fun getUsers(): List<User> {
        println("LOGGING ::: DB 연동 TEST")
        return userService.getUsers()
    }

    @PostMapping("/test")
    fun saveUser(
        @RequestBody request: UserRequest,
    ) = userService.saveUser(request)
}
