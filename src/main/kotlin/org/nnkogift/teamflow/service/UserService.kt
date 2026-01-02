package org.nnkogift.teamflow.service

import org.nnkogift.teamflow.dto.UpdateUserRequest
import org.nnkogift.teamflow.dto.UserResponse
import org.nnkogift.teamflow.exception.NotFoundException
import org.nnkogift.teamflow.repository.UserRepository
import org.nnkogift.teamflow.security.SecurityUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getCurrentUser(): UserResponse {
        val user = SecurityUtils.getCurrentUser()
        return user.toResponse()
    }

    @Transactional
    fun updateCurrentUser(request: UpdateUserRequest): UserResponse {
        val userId = SecurityUtils.getCurrentUserId()
        val user = userRepository.findById(userId).orElseThrow { NotFoundException("User not found") }

        if (request.email != null && request.email != user.email) {
            userRepository.findByEmail(request.email)
                .ifPresent { throw IllegalArgumentException("Email already exists") }
        }
        val updatedUser = user.copy(
            name = request.name ?: user.name,
            email = request.email ?: user.email,
            avatarUrl = request.avatarUrl ?: user.avatarUrl
        )

        return userRepository.save(updatedUser).toResponse()
    }
}