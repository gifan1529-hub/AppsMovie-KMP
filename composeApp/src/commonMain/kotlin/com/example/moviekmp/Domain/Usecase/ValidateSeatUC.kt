package com.example.moviekmp.Domain.Usecase

class ValidateSeatUC () {

    operator fun invoke(totalTickets: Int, selectedSeats: Int): Boolean {
        return totalTickets > 0 && selectedSeats == totalTickets
    }
}