package com.example.moviekmp.Domain.Usecase

import androidx.datastore.core.DataStore
import com.example.moviekmp.Data.Local.PrefsManager

class LogoutUserUC (
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke() {
        prefsManager.logoutUser()
    }
}