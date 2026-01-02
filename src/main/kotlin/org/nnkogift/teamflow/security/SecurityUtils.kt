package org.nnkogift.teamflow.security

import org.nnkogift.teamflow.entity.User
import org.nnkogift.teamflow.exception.UnauthorizedException
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {

    fun getCurrentUser(): User {
        val authentication =
            SecurityContextHolder.getContext().authentication ?: throw RuntimeException("No authentication found")

        val userDetails = authentication.principal as? CustomUserDetails
            ?: throw UnauthorizedException("Invalid authentication implementation")

        return userDetails.getUser()
    }

    fun getCurrentUserId(): Long {
        return getCurrentUser().id ?: throw UnauthorizedException("User ID not found")
    }

    fun getCurrentUserEmail(): String {
        return getCurrentUser().email
    }
}