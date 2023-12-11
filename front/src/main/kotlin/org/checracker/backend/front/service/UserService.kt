package org.checracker.backend.front.service

import org.checracker.backend.core.repository.user.UserRepository
import org.checracker.backend.front.model.request.UserRequest
import org.checracker.backend.front.model.request.toEntity
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getUsers() = userRepository.findAll()

    fun saveUser(request: UserRequest) = userRepository.save(request.toEntity())
}
