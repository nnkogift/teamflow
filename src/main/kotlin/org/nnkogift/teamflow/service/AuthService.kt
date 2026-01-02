package org.nnkogift.teamflow.service

import org.nnkogift.teamflow.dto.*
import org.nnkogift.teamflow.entity.User
import org.nnkogift.teamflow.exception.BadRequestException
import org.nnkogift.teamflow.exception.NotFoundException
import org.nnkogift.teamflow.repository.UserRepository
import org.nnkogift.teamflow.security.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*


@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw BadRequestException("Email already exists")
        }

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password) as String,
            name = request.name
        )

        val savedUser = userRepository.save(user)

        val token = jwtUtil.generateToken(savedUser.email)
        val refreshToken = jwtUtil.generateRefreshToken(savedUser.email)

        return AuthResponse(
            token = token,
            refreshToken = refreshToken,
            user = savedUser.toResponse()
        )
    }


    fun login(request: LoginRequest): AuthResponse {
        val user =
            userRepository.findByEmail(request.email).orElseThrow { BadRequestException("Invalid email or password") }
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BadRequestException("Invalid email or password")
        }
        val token = jwtUtil.generateToken(user.email)
        val refreshToken = jwtUtil.generateRefreshToken(user.email)
        return AuthResponse(token, refreshToken, user.toResponse())

    }

    fun refreshToken(request: RefreshTokenRequest): AuthResponse {
        val email = jwtUtil.extractEmail(request.refreshToken)
        val user = userRepository.findByEmail(email).orElseThrow { BadRequestException("Invalid refresh token") }
        val newToken = jwtUtil.generateToken(user.email)
        return AuthResponse(token = newToken, refreshToken = request.refreshToken, user = user.toResponse())
    }


    @Transactional
    fun forgotPassword(request: ForgotPasswordRequest): MessageResponse {
        val user = userRepository.findByEmail(request.email).orElseThrow { NotFoundException("Invalid email") }

        val resetToken = UUID.randomUUID().toString()
        val tokenExpiry = LocalDateTime.now().plusHours(1)

        val updatedUser = user.copy(resetToken = resetToken, resetTokenExpiry = tokenExpiry)
        userRepository.save(updatedUser)
        println("Password reset token for ${user.email}: $resetToken")
        println("Reset link: http://localhost:8080/reset-password?token=$resetToken")

        return MessageResponse("Password reset email sent. Check your inbox for instructions.")
    }

    @Transactional
    fun resetPassword(request: ResetPasswordRequest): MessageResponse {
        val user = userRepository.findByResetToken(request.token)
            .orElseThrow { BadRequestException("Invalid or expired token") }

        val resetToken: String = user.resetToken ?: throw BadRequestException("Invalid or expired token")
        val resetTokenExpiry: LocalDateTime =
            user.resetTokenExpiry ?: throw BadRequestException("Invalid or expired token")

        if (resetTokenExpiry.isBefore(LocalDateTime.now())) {
            throw BadRequestException("Reset token has expired")
        }
        if (resetToken != request.token) {
            throw BadRequestException("Invalid token")
        }

        val updatedUser = user.copy(
            password = passwordEncoder.encode(request.newPassword) as String,
            resetToken = null,
            resetTokenExpiry = null
        )

        userRepository.save(updatedUser)
        return MessageResponse("Password reset successful")
    }
}