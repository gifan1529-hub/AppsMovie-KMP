package com.example.moviekmp.DI

expect object NotificationHelper {
    fun showSuccessNotification(movieTitle: String, theater: String, bookingId: Int)

}