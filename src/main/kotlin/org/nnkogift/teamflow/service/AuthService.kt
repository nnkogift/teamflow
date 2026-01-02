package org.nnkogift.teamflow.service

import org.nnkogift.teamflow.dto.*
import org.nnkogift.teamflow.entity.User
import org.nnkogift.teamflow.exception.BadRequestException
import org.nnkogift.teamflow.repository.UserRepository
import org.nnkogift.teamflow.security.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


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

    private fun User.toResponse() = UserResponse(
        id = id!!,
        email = email,
        name = name,
        avatarUrl = avatarUrl,
        role = role.name
    )
}