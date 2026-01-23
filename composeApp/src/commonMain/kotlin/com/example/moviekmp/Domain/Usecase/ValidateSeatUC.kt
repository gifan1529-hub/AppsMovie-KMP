package com.example.moviekmp.Domain.Usecase

/**
 * usecase untuk validasi seat
 * seat hanya bisa di pilih seusi dengan jumlah ticket
 */
class ValidateSeatUC () {

    operator fun invoke(totalTickets: Int, selectedSeats: Int): Boolean {
        return totalTickets > 0 && selectedSeats == totalTickets
    }
}