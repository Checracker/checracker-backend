package org.checracker.backend.front.service

import org.checracker.backend.core.repository.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun getUsers() = userRepository.findAll()
}
