package org.checracker.backend.core.user.repository

import org.checracker.backend.core.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findUserById(id: Long): User?
}
