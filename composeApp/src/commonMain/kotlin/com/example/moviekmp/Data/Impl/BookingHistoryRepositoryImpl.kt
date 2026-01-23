package com.example.moviekmp.Data.Impl

import com.example.moviekmp.Data.Local.BookingHistoryDao
import com.example.moviekmp.Domain.Model.BookingHistory
import com.example.moviekmp.Domain.Repository.BookingHistoryRepository
import kotlinx.coroutines.flow.Flow

/**
 * implementasi dari [BookingHistoryRepository]
 */
class BookingHistoryRepositoryImpl (
    private val bookingHistoryDao: BookingHistoryDao
) : BookingHistoryRepository {
    /**
     * ngambil data booking dari database sesuai sama email nya
     */
    override fun getBookingsByEmail(email: String): Flow<List<BookingHistory>> {
        return bookingHistoryDao.getBookingsByEmail(email)
    }
}