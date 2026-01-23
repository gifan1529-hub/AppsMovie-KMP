package com.example.moviekmp.DI

import com.example.moviekmp.Domain.Model.BookingHistory
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUserDomainMask
import platform.Foundation.writeToFile
import platform.UIKit.NSFontAttributeName
import platform.UIKit.UIFont
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIGraphicsPDFRenderer
import platform.UIKit.UIGraphicsPDFRendererFormat
import platform.UIKit.drawAtPoint

/**
 * ini buat export ke pdf di ios
 * file nya bakal kesimpen di lokasi yang udah di set
 */
actual class ExportPdf {

    @OptIn(ExperimentalForeignApi::class)
    actual fun exportTicketPdf(booking: BookingHistory) {
        val format = UIGraphicsPDFRendererFormat() // bawaan ios
        val pageWidth = 595.0
        val pageHeight = 842.0
        val renderer = UIGraphicsPDFRenderer(
            bounds = CGRectMake(0.0, 0.0, pageWidth, pageHeight),
            format = format
        )

        val data = renderer.PDFDataWithActions { context ->
            context?.beginPage()
            val attributes: Map<Any?, *>? = mapOf(
                NSFontAttributeName to UIFont.boldSystemFontOfSize(18.0))
            // judul film ke NSString biar bisa di gambar
            (booking.movieTitle as NSString).drawAtPoint(
                CGPointMake( // biar bisa nentuin x, y nya
                    100.0,
                    100.0
                ), withAttributes = attributes)
        }

        // nentuin lokasi nympen file
        val path = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        ).first() as String
        // nama file
        val fileName = "$path/Tiket_${booking.id}.pdf"
        // nyimpen data pdf ke memori ipon
        data.writeToFile(fileName, true)

    }
}