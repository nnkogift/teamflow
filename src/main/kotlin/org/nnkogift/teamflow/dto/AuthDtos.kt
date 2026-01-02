package org.nnkogift.teamflow.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class RegisterRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String,

    @field:Size(min = 8, message = "Password must be at least 8 characters")
    @field:NotBlank(message = "Password is required")
    val password: String
)


data class LoginRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)

data class AuthResponse(val token: String, val refreshToken: String, val user: UserResponse)

data class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val role: String
)

data class RefreshTokenRequest(
    @field:NotBlank(message = "Refresh token is required")
    val refreshToken: String
)

data class UpdateUserRequest(
    val name: String? = null,

    @field:Email(message = "Invalid email format")
    val email: String? = null,

    val avatarUrl: String? = null
)