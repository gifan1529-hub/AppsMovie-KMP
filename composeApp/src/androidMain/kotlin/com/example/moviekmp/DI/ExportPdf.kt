package com.example.moviekmp.DI

import android.content.ContentValues
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.moviekmp.Data.Local.MyMovieApp
import com.example.moviekmp.Domain.Model.BookingHistory
import java.io.FileOutputStream

actual class ExportPdf {
    actual fun exportTicketPdf(booking: BookingHistory) {
        val context = MyMovieApp.appContext
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 500, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // Judul
        paint.textSize = 16f
        paint.isFakeBoldText = true
        canvas.drawText("TICKET BIOSKOP", 80f, 50f, paint)

        // Detail Tiket
        paint.textSize = 12f
        paint.isFakeBoldText = false
        canvas.drawText("Film: ${booking.movieTitle}", 20f, 100f, paint)
        canvas.drawText("Theater: ${booking.theater}", 20f, 130f, paint)
        canvas.drawText("Sesi: ${booking.session}", 20f, 160f, paint)
        canvas.drawText("Kursi: ${booking.seatIds.joinToString()}", 20f, 190f, paint)
        canvas.drawText("Total: Rp ${booking.totalPrice}", 20f, 220f, paint)

        pdfDocument.finishPage(page)

        // Simpan ke folder Downloads
        val fileName = "Tiket_${booking.movieTitle}_${booking.id}.pdf"

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                }
            } else {
                val file = java.io.File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), fileName)
                pdfDocument.writeTo(FileOutputStream(file))
            }
        } finally {
            pdfDocument.close()
        }
    }
}