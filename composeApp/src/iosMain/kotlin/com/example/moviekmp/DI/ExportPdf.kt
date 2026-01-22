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

actual class ExportPdf {

    @OptIn(ExperimentalForeignApi::class)
    actual fun exportTicketPdf(booking: BookingHistory) {
        val format = UIGraphicsPDFRendererFormat()
        val pageWidth = 595.0
        val pageHeight = 842.0
        val renderer = UIGraphicsPDFRenderer(
            bounds = CGRectMake(0.0, 0.0, pageWidth, pageHeight),
            format = format
        )

        val data = renderer.PDFDataWithActions { context ->
            context?.beginPage()
            val attributes: Map<Any?, *>? = mapOf(NSFontAttributeName to UIFont.boldSystemFontOfSize(18.0))
            (booking.movieTitle as NSString).drawAtPoint(CGPointMake(100.0, 100.0), withAttributes = attributes)
        }

        val path = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true).first() as String
        val fileName = "$path/Tiket_${booking.id}.pdf"
        data.writeToFile(fileName, true)

    }
}