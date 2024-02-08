package org.checracker.backend.front.model.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class LocalAuthSignInRequest(
    @NotBlank
    @field:Email
    val email: String,

    @NotBlank
    @field:Size(min = 7, max = 25)
    val password: String,
)
