package org.nnkogift.teamflow.controller

import jakarta.validation.Valid
import org.nnkogift.teamflow.dto.AuthResponse
import org.nnkogift.teamflow.dto.ForgotPasswordRequest
import org.nnkogift.teamflow.dto.LoginRequest
import org.nnkogift.teamflow.dto.MessageResponse
import org.nnkogift.teamflow.dto.RefreshTokenRequest
import org.nnkogift.teamflow.dto.RegisterRequest
import org.nnkogift.teamflow.dto.ResetPasswordRequest
import org.nnkogift.teamflow.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {



    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.login(request))
    }
    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.refreshToken(request))
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(@Valid @RequestBody request: ForgotPasswordRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(authService.forgotPassword(request))
    }

    @PostMapping("/reset-password")
    fun resetPassword(@Valid @RequestBody request: ResetPasswordRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(authService.resetPassword(request))
    }
}