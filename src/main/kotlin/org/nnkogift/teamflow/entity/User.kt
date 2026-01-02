package org.nnkogift.teamflow.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.nnkogift.teamflow.dto.UserResponse
import java.time.LocalDateTime


@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(unique = true, nullable = false) val email: String,
    @Column(nullable = false) val password: String,
    @Column(nullable = false) val name: String,
    val avatarUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole = UserRole.MEMBER,
    val isEmailVerified: Boolean = false,
    val resetToken: String? = null,
    val resetTokenExpiry: LocalDateTime? = null,

    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null
) {

    fun toResponse() = UserResponse(
        id = id!!,
        email = email,
        name = name,
        avatarUrl = avatarUrl,
        role = role.name
    )
}

enum class UserRole {
    ADMIN, TEAM_LEAD, MEMBER
}