package com.example.moviekmp.DI
/**
 * expect untuk Notification di android dan ios
 */
expect object NotificationHelper {
    fun showSuccessNotification(movieTitle: String, theater: String, bookingId: Int)

}