package com.example.moviekmp.Domain.Usecase

import com.example.moviekmp.Domain.Model.User
import com.example.moviekmp.Domain.Repository.UserRepository

sealed class RegistrationStatus {
    object Success : RegistrationStatus()
    data class Error(val message: String) : RegistrationStatus()
    data class Failure(val error: Exception) : RegistrationStatus()
}

class RegisterUserUC (
    private val repository: UserRepository
){
    suspend operator fun invoke(user: User): RegistrationStatus {
        if (user.email.isBlank()|| user.userPassword.isBlank()) {
            return RegistrationStatus.Error("Semua Field Harus Diisi")
        }
        if (repository.findUserByEmail(user.email) != null) {
            return RegistrationStatus.Error("Email Sudah Terdaftar")
        }

        return try {
            repository.insertUser(user)
            RegistrationStatus.Success
        } catch (e: Exception) {
            RegistrationStatus.Failure(e)
        }
    }
}