package org.nnkogift.teamflow.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ForgotPasswordRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String
)


data class ResetPasswordRequest(
    @field:NotBlank(message = "Token is required")
    val token: String,
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    @field:NotBlank(message = "New password is required")
    val newPassword: String
)

data class MessageResponse(val message: String)