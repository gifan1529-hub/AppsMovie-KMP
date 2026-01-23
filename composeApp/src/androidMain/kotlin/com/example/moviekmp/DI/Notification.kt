package com.example.moviekmp.DI

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.example.moviekmp.Data.Local.MyMovieApp
import com.example.moviekmp.MainActivity
import com.example.moviekmp.R

/**
 * nampilin notifikasi setelah mesan ticket di anddroid
 */
actual object NotificationHelper {

   actual fun showSuccessNotification(movieTitle: String, theater: String, bookingId: Int) {
        Log.d("Notification", "${movieTitle}")
       val uniqueNotifId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
       val context = MyMovieApp.Companion.appContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val routeUri = "app://movie/detailticket/$bookingId".toUri()

        val intent = Intent(
            Intent.ACTION_VIEW,
            routeUri,
            context,
            MainActivity::class.java
        )

        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                bookingId,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(context, "PYAMENT_SUCCES_CHANNEL")
            .setSmallIcon(R.drawable.ticket)
            .setContentTitle("Pembayaran Berhasil! 🍿")
            .setContentText("Tiket untuk $movieTitle di $theater berhasil dipesan.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(bookingId, notification)
       Log.d("Notification", "${uniqueNotifId}")

    }
}