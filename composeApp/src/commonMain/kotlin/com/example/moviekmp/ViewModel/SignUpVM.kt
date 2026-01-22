package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Domain.Model.User
import com.example.moviekmp.Domain.Usecase.RegisterUserUC
import com.example.moviekmp.Domain.Usecase.RegistrationStatus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignUpUiState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val isLoading: Boolean = false
)

class SignUpVM(
    private val registerUserUC: RegisterUserUC
): ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RegistrationStatus>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(emailValue = email) }

    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(passwordValue = password) }
    }

    fun onSignUpClick(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userToRegister = User(
                userPassword = uiState.value.passwordValue,
                email = uiState.value.emailValue
            )
            val result = registerUserUC(userToRegister)
            _eventFlow.emit(result)
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}