package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Data.Local.PrefsManager
import com.example.moviekmp.Domain.Model.BookingHistory
import com.example.moviekmp.Domain.Usecase.GetBookingUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

/**
 * viewModel dari ticket screen
 * nampilin booking history sesuai sama email nya dari data store
 */
class TicketVM (
    private val getBookingsByEmail : GetBookingUC,
    private val prefsManager: PrefsManager
) : ViewModel() {

    private val userEmail = prefsManager.getUserEmail() ?: ""

    @OptIn(ExperimentalCoroutinesApi::class)
    val ticketsFlow: Flow<List<BookingHistory>> = prefsManager.getUserEmail()
        .flatMapLatest { email ->
            getBookingsByEmail(email)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
