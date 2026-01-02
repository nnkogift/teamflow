package org.nnkogift.teamflow.repository

import org.nnkogift.teamflow.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun existsByEmail(email: String): Boolean
    fun findByResetToken(token: String): Optional<User>
}