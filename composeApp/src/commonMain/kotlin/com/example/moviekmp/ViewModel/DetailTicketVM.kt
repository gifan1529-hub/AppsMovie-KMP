package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Domain.Model.BookingHistory
import com.example.moviekmp.Domain.Usecase.GetBookingDetailUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.moviekmp.DI.ExportPdf

/**
 * viewModel dari detail ticket
 */
class DetailTicketVM  (
    private val getBookingDetailUC: GetBookingDetailUC
) : ViewModel() {
    private val _ticketDetails = MutableStateFlow<BookingHistory?>(null)
    val ticketDetails: StateFlow<BookingHistory?> = _ticketDetails
    val exportPdf = ExportPdf()

    /**
     * ngambil data detail ticket sesuai sama id nya
     */
    fun loadBookingDetails(id: Int) {
        viewModelScope.launch {
            val details = getBookingDetailUC(id)
            _ticketDetails.value = details
        }
    }

    /**
     * function untuk export to pdf
     */
    fun downloadPdf(ticket: BookingHistory?){
        ticket?.let{
            exportPdf.exportTicketPdf(it)
        }

    }
}