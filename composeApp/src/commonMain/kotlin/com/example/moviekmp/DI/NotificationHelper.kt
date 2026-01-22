package com.example.moviekmp.DI

expect object NotificationHelper {
    fun showSuccessNotification(movieTitle: String, theater: String, bookingId: Int)
//    val notification = UNMutableNotificationContent().apply{
//        setTitle("Pembayaran Berhasil! 🍿")
//        setBody("Tiket untuk $movieTitle di $theater berhasil dipesan.")
//        setSound(UNNotificationSound.defaultSound()))
//    }
}