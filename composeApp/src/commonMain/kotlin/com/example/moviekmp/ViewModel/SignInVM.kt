package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Data.Local.PrefsManager
import com.example.moviekmp.Domain.Usecase.LoginResult
import com.example.moviekmp.Domain.Usecase.LoginUC
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignInUiState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val isLoading: Boolean = false
)

class SignInVM (
    private val loginUserUC: LoginUC,
    private val prefsManager: PrefsManager // pengganti shared prefs
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<LoginResult>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            currentState.copy(emailValue = email)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(passwordValue = password)
        }
    }

    fun onSignInClick(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = loginUserUC(_uiState.value.emailValue, _uiState.value.passwordValue)
            if (result is LoginResult.Success) {
                prefsManager.createLoginSession(
                    id = 0,
                    email = _uiState.value.emailValue,
                    password = _uiState.value.passwordValue
                )
            }
            _eventFlow.emit(result)
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}