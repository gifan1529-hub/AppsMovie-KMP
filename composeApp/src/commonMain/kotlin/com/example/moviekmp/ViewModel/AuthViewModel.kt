package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.moviekmp.Data.Local.PrefsManager

class AuthViewModel (
    private val prefs: PrefsManager
) : ViewModel()  {
    suspend fun isLoggedIn(): Boolean {
        return prefs.isLoggedIn()
    }

    suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        prefs.saveLoginStatus(isLoggedIn)
    }
}