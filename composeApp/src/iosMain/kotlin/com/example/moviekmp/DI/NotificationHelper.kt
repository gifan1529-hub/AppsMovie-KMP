package com.example.moviekmp.DI

import kotlinx.serialization.json.JsonNull.content
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

/**
 * Helper ini  buat nampilin notification di ios
 * kalo di pencet notifnya bakal ke detail ticket sesuai sama booking id nya
 */
actual object NotificationHelper {
    actual fun showSuccessNotification(movieTitle: String, theater: String, bookingId: Int) {
        val notification = UNMutableNotificationContent().apply{
            setTitle("Pembayaran Berhasil! 🍿")
            setBody("Tiket untuk $movieTitle di $theater berhasil dipesan.")
            setSound(UNNotificationSound.defaultSound())

            setUserInfo(mapOf("bookingId" to bookingId.toString()))
        }
        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(1.0, false)
        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = "booking_$bookingId",
            content = notification,
            trigger = trigger
        )

        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) {error ->
            if (error != null) {
                println("Error nih: ${error.localizedDescription}")
            }
        }
    }
}