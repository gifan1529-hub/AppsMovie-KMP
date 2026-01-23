package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Data.Local.PrefsManager
import com.example.moviekmp.Domain.Model.User
import com.example.moviekmp.Domain.Usecase.GetUserUC
import com.example.moviekmp.Domain.Usecase.UpdateUserUC
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * state awal dari email dan password
 */
data class EditUserUiState(
    val email: String = "",
    val passwordBaru: String = "",
    val isLoading: Boolean = false,
    val updateSuccess: Boolean = false,
    val error: String? = null
)

/**
 * viewmodel dari edit user
 */
class EditUserVM (
    private val getUserUC: GetUserUC,
    private val updateUserUC: UpdateUserUC,
    private val prefsManager: PrefsManager
): ViewModel() {
    private val _uiState = MutableStateFlow<EditUserUiState>(EditUserUiState())
    val uiState: StateFlow<EditUserUiState> get() = _uiState

    private var currentUser : User? = null

    init {
        _uiState.value = EditUserUiState()
    }

    /**
     * ngambil data user dari database room sesuai sama email nya
     */
    fun loadUser(userEmail: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            currentUser = getUserUC(userEmail)
            println("DEBUG: Hasil dari Room untuk $userEmail adalah $currentUser")
            currentUser?.let { user ->
                _uiState.value = _uiState.value.copy(
                    email = user.email,
                    passwordBaru = user.userPassword,
                    isLoading = false
                )
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    error = "User not found",
                    isLoading = false
                )
            }
        }
    }

    /**
     * memperbarui state email sesuai sama yang user ketik
     */
    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    /**
     * memperbarui state password sesuai sama yang user ketik
     */
    fun onPasswordBaruChanged(passwordBaru: String) {
        _uiState.value = _uiState.value.copy(passwordBaru = passwordBaru)
    }

    /**
     * nyimpen data ke database room
     * nge copy email dan password yang baru ke database room dan disimpan ke data store
     */
    fun onUpdateClicked() {
        viewModelScope.launch {
            val currentState = _uiState.value ?: return@launch
            val originalUser = currentUser ?: return@launch

            val newEmail = currentState.email
            val passwordUntukDisimpan = currentState.passwordBaru.ifBlank {
                originalUser.userPassword
            }

            val updatedUser = originalUser.copy(
                email = newEmail,
                userPassword = passwordUntukDisimpan
            )

            _uiState.value = currentState.copy(isLoading = true)

            try {
                updateUserUC(updatedUser)

//                Log.d("DEBUGss", "Menyimpan ke SharedPrefs: $newEmail")
                prefsManager.saveEmail(newEmail)

                delay(50)

                _uiState.value = currentState.copy(
                    updateSuccess = true,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}