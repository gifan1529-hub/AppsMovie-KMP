package com.example.moviekmp.Domain.Repository

import com.example.moviekmp.Domain.Model.BookingHistory
import kotlinx.coroutines.flow.Flow

interface BookingHistoryRepository {

    fun getBookingsByEmail(email: String): Flow<List<BookingHistory>>
}