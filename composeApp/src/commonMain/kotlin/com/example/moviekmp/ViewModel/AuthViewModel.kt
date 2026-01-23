package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.moviekmp.Data.Local.PrefsManager

/**
 * view model untuk auth
 */
class AuthViewModel (
    private val prefs: PrefsManager
) : ViewModel()  {
    /**
     * ngecek apakah user udah login atau belum
     */
    suspend fun isLoggedIn(): Boolean {
        return prefs.isLoggedIn()
    }
}