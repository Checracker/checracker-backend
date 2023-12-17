package org.checracker.backend.core.repository.user

import org.checracker.backend.core.entity.user.User
import org.checracker.backend.core.enum.Provider
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findUserById(id: Long): User?

    fun findByEmailAndProvider(email: String, provider: String): User?
}
