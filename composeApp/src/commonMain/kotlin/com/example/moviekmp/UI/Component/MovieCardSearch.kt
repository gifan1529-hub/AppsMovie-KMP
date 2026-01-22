package com.example.moviekmp.UI.Component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.example.moviekmp.Domain.Model.RoomApi
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun MovieCardSearch(
    movie: RoomApi,
    onClick: () -> Unit
) {
    val Poppins = getPoppinsFontFamily()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row() {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .width(120.dp)
                    .height(200.dp)
                    .clickable(onClick = onClick),
                shape = RoundedCornerShape(12.dp)
            ) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.bintang),
                    error = painterResource(Res.drawable.bintang)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column() {
                Text(
                    text = movie.title ?: "",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(
                    text = movie.title ?: "",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    color = Color.White
                )
                Text(
                    text = movie.releaseYear?.toString() ?: "-",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.White
                )
                Text(
                    text =movie.plot ?: "",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.White,
                    lineHeight = 13.sp
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}