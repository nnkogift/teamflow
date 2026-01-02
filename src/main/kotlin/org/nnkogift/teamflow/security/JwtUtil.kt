package org.nnkogift.teamflow.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey


@Component
class JwtUtil(
    @Value("\${app.jwt.secret}")
    private val secret: String,
    @Value("\${app.jwt.expiration}")
    private val expiration: Long,
    @Value("\${app.jwt.refresh-expiration}")
    private val refreshExpiration: Long
) {
    private val key: SecretKey by lazy { Keys.hmacShaKeyFor(secret.toByteArray()) }

    fun generateToken(email: String): String {
        return createToken(email, expiration)
    }

    fun generateRefreshToken(email: String): String {
        return createToken(email, refreshExpiration)
    }

    private fun createToken(subject: String, expirationTime: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationTime)
        return Jwts.builder()
            .subject(subject)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun extractEmail(token: String): String {
        return extractClaims(token).subject
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)
        return email == userDetails.username && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String): Boolean {
        return extractClaims(token).expiration.before(Date())
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
    }
}