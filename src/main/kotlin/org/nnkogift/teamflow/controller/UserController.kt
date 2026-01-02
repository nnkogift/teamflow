package org.nnkogift.teamflow.controller

import jakarta.validation.Valid
import org.nnkogift.teamflow.dto.UpdateUserRequest
import org.nnkogift.teamflow.dto.UserResponse
import org.nnkogift.teamflow.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/me")
    fun getCurrentUser(): ResponseEntity<UserResponse> = ResponseEntity.ok(userService.getCurrentUser())

    @PutMapping("/me")
    fun updateUser(@Valid @RequestBody request: UpdateUserRequest): ResponseEntity<UserResponse> =
        ResponseEntity.ok(userService.updateCurrentUser(request))
}