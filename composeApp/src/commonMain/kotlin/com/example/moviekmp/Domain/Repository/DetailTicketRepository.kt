package com.example.moviekmp.Domain.Repository

import com.example.moviekmp.Domain.Model.BookingHistory

/**
 * interface dari [UserRepository]
 */
interface DetailTicketRepository {
    suspend fun getBookingById(id: Int): BookingHistory?
}