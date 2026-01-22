package com.example.moviekmp.Domain.Usecase

import com.example.moviekmp.Domain.Model.User
import com.example.moviekmp.Domain.Repository.EditUserRepository

class UpdateUserUC (
    private val userRepository: EditUserRepository
) {
    suspend operator fun invoke(user: User) {
        userRepository.updateUser(user)
    }
}