package org.checracker.backend.front.controller

import org.checracker.backend.front.model.request.UserRequest
import org.checracker.backend.front.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/test")
    fun getUsers() = userService.getUsers()

    @PostMapping("/test")
    fun saveUser(request: UserRequest) = userService.saveUser(request)
}
