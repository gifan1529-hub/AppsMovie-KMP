package com.example.moviekmp.DI

import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationSound

actual object NotificationHelper {
    actual fun showSuccessNotification(movieTitle: String, theater: String, bookingId: Int) {
        val notification = UNMutableNotificationContent().apply{
            setTitle("Pembayaran Berhasil! 🍿")
            setBody("Tiket untuk $movieTitle di $theater berhasil dipesan.")
            setSound(UNNotificationSound.defaultSound())
        }
    }
}