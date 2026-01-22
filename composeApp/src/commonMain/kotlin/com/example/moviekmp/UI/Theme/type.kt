package com.example.moviekmp.UI.Theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import moviekmp.composeapp.generated.resources.*

@Composable
fun getPoppinsFontFamily() = FontFamily(
    Font(Res.font.poppinsregular, FontWeight.Normal),
    Font(Res.font.poppinsmedium, FontWeight.Medium),
    Font(Res.font.poppinssemibold, FontWeight.SemiBold),
    Font(Res.font.poppinsbold, FontWeight.Bold),
    Font(Res.font.poppinsextra, FontWeight.ExtraBold)
)

@Composable
fun getTypography(): Typography {
    val Poppins = getPoppinsFontFamily()

    return Typography(
        bodyLarge = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        titleLarge = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}