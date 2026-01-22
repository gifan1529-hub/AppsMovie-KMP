package com.example.moviekmp.Domain.Usecase

import com.example.moviekmp.Domain.Model.BookingHistory
import com.example.moviekmp.Domain.Repository.DetailTicketRepository

class GetBookingDetailUC (
    private val repository: DetailTicketRepository
) {
    suspend operator fun invoke(id: Int): BookingHistory? {
        return repository.getBookingById(id)
    }
}