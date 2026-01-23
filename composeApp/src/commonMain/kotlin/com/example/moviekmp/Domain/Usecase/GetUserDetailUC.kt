package com.example.moviekmp.Domain.Usecase

import com.example.moviekmp.Data.Local.PrefsManager
import kotlinx.coroutines.flow.first

data class UserDetails(
    val email : String
)

/**
 * usecase untuk mengambil data user berdasarkan email
 */
class GetUserDetailsUC (
    private val prefsManager: PrefsManager
) {
    suspend fun invoke(): UserDetails? {
        val userEmail = prefsManager.getUserEmail().first()

        return if ( userEmail.isNotBlank() && userEmail != "user Email Tidak ketemu") {
            UserDetails(email = userEmail)
        } else {
            null
        }
    }
}