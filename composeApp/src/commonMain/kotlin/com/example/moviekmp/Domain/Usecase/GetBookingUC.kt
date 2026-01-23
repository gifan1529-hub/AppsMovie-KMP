package com.example.moviekmp.Domain.Usecase

import com.example.moviekmp.Domain.Model.BookingHistory
import com.example.moviekmp.Domain.Repository.BookingHistoryRepository
import kotlinx.coroutines.flow.Flow

/**
 * use case untuk mengambil daftar booking berdasarkan email
 */
class GetBookingUC (
    private val bookingHistoryRepository: BookingHistoryRepository
) {
    operator fun invoke(email: String): Flow<List<BookingHistory>> {
        return bookingHistoryRepository.getBookingsByEmail(email)
    }
}