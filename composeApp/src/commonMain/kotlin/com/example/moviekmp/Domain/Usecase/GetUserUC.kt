package com.example.moviekmp.Domain.Usecase

import com.example.moviekmp.Domain.Model.User
import com.example.moviekmp.Domain.Repository.EditUserRepository

class GetUserUC (
    private val userRepository: EditUserRepository
) {
    suspend operator fun invoke (email: String): User? {
        return userRepository.getUserByEmail(email)
    }
}