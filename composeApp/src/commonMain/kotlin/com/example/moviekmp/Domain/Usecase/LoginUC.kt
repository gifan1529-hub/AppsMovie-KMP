package com.example.moviekmp.Domain.Usecase

import com.example.moviekmp.Domain.Repository.UserRepository

sealed class LoginResult {
    data class Success(val email: String) : LoginResult()
    data class Error(val message: String) : LoginResult()
    data class Failure(val error: Exception) : LoginResult()
}

/**
 * usecase untuk ngecek apakah user ada di database atau tidak
 */
class LoginUC (
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        if (email.isBlank()) {
            return LoginResult.Error("Email harus diisi")
        }
        if (password.isBlank()) {
            return LoginResult.Error("Password harus diisi")
        }

        return try {
            val user = repository.findUserByEmail(email)

            if (user != null && user.userPassword == password) {
                LoginResult.Success(user.email!!)
            } else {
                LoginResult.Error("Email atau password salah")
            }
        } catch (e: Exception) {
            LoginResult.Failure(e)
        }
    }
}