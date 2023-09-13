package org.checracker.backend.front.model.request

import org.checracker.backend.core.user.entity.User

data class UserRequest(
    val id: Long? = null,
    val name: String
)

fun UserRequest.toEntity() = User(
    id = id,
    name = name
)
