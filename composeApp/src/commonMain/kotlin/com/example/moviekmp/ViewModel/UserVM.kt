package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Data.Local.PrefsManager
import com.example.moviekmp.Domain.Usecase.GetUserDetailsUC
import com.example.moviekmp.Domain.Usecase.LogoutUserUC
import com.example.moviekmp.Domain.Usecase.UserDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserVM (
    private val getUser: GetUserDetailsUC,
    private val logoutUser: LogoutUserUC,
    private val prefsManager: PrefsManager
) : ViewModel() {
    private val _userDetails = MutableStateFlow<UserDetails?>(null)
    val userDetails: StateFlow<UserDetails?> = _userDetails

    private val _logoutEvent = MutableStateFlow<Boolean>(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent

    fun loadUserDetail(){
        viewModelScope.launch {
            delay(100)
            val email = prefsManager.getUserEmail().first()
//            Log.d("DEBUGss", "Loading data for email: $email")
            val details = getUser.invoke()
            _userDetails.value = details
        }
    }
    fun logout() {
        viewModelScope.launch {
            logoutUser()
            _logoutEvent.value = true
        }
    }
}