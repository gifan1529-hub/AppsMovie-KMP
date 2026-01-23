package com.example.moviekmp.DI

import com.example.moviekmp.Domain.Model.BookingHistory

/**
 * expect untuk ExportPdf di android dan ios
 */
expect class ExportPdf() {
    fun exportTicketPdf(booking: BookingHistory)
}