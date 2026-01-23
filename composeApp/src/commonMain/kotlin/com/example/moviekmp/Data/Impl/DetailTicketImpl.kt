package com.example.moviekmp.Data.Impl

import com.example.moviekmp.Data.Local.BookingHistoryDao
import com.example.moviekmp.Domain.Model.BookingHistory
import com.example.moviekmp.Domain.Repository.DetailTicketRepository

/**
 * implementasi dari [DetailTicketRepository]
 */
class DetailTicketImpl (
    private val bookingHistoryDao: BookingHistoryDao
) : DetailTicketRepository {
    /**
     * ngambil data booking dari database sesuai sama id nya
     */
    override suspend fun getBookingById(id: Int): BookingHistory? {
        return bookingHistoryDao.getBookingById(id)
    }
}