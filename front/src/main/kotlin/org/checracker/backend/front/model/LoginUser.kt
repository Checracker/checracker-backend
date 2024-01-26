package org.checracker.backend.front.model

import org.checracker.backend.core.entity.user.User
import org.springframework.security.core.GrantedAuthority

class LoginUser(
    val id: Long,
    val provider: String,
    val email: String,
    val name: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>,
) : org.springframework.security.core.userdetails.User(
    id.toString(),
    password,
    authorities,
)

fun User.toLoginUser() = LoginUser(
    id = this.id ?: 0,
    password = password,
    authorities = authorities,
    provider = provider,
    email = email,
    name = name,
)
