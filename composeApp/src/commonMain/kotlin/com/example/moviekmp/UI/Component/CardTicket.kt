package com.example.moviekmp.UI.Component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.moviekmp.Domain.Model.BookingHistory
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily


@Composable
fun CardTicket(
    ticket: BookingHistory,
    onClick: () -> Unit
){
    val Poppins = getPoppinsFontFamily()
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .width(355.dp)
            .height(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ticket.moviePosterUrl,
                contentDescription = "URL",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .padding(top = 120.dp, start = 20.dp)
            ) {
                Text(
                    text = ticket.movieTitle,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = ticket.session,
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
                Text(
                    text = ticket.theater,
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
        }
    }
}