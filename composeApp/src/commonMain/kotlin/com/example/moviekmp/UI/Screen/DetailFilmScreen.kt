package com.example.moviekmp.UI.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.moviekmp.Domain.Usecase.DetailResult
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily
import com.example.moviekmp.ViewModel.BookingTicketVM
import com.example.moviekmp.ViewModel.DetailFilmVM
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailFilmScreen(
    navController: NavController,
    movieId: String,
    viewModel: DetailFilmVM = koinViewModel(),
    bookingVM: BookingTicketVM = koinViewModel()
) {

    LaunchedEffect(movieId) {
        viewModel.getMovieById(movieId)
    }

    val Poppins = getPoppinsFontFamily()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        val movie = (uiState as? DetailResult.Success)?.movie
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    AsyncImage(
                        model = movie?.posterUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xFF1A1A1A)),
                                    startY = 100f
                                )
                            )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.background(Color.Black.copy(0.4f), CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                        IconButton(
                            onClick = {
                                movie?.let { viewModel.toggleFavoriteStatus(it) }
                            },
                            modifier = Modifier
                                .background(Color.Black.copy(0.4f), CircleShape)
                        ) {
                            Icon(
                                painter = if (isFavorite) painterResource(Res.drawable.star)  else painterResource(Res.drawable.bintang),
                                contentDescription = null,
                                tint = Color(0xFFFFCC00),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                    }

                    AsyncImage(
                        model = movie?.posterUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .size(width = 140.dp, height = 210.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .align(Alignment.BottomStart),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 180.dp, bottom = 10.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        Text(
                            text = movie?.title ?: "Loading...",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = movie?.releaseYear?.toString() ?: "Loading...",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = Poppins
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val ratingValue = movie?.rating ?: 0.0
                            val filledStars = (ratingValue / 2).toInt()
                            repeat(5) { index ->
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < filledStars) Color(0xFFFFCC00) else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "$ratingValue/10",
                                color = Color.Gray,
                                fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Genre",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        fontFamily = Poppins
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = movie?.genre ?: "Loading...",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Poppins)
                }
            }
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(text = "Plot",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        fontFamily = Poppins)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie?.plot ?: "Loading...",
                        color = Color.LightGray,
                        lineHeight = 22.sp,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Poppins
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
        Button(
            onClick = {
                if (movie != null) {
                    bookingVM.setInitialMovieData(
                        id = movie.id,
                        title = movie.title,
                        posterUrl = movie.posterUrl,
                        genre = movie.genre
                    )
                    println("qwerty : ${movie.id}${movie.title}${movie.posterUrl}${movie.genre}")
//                    Log.d("DetailFilmScreen", "data ${movie.id}${movie.title}${movie.posterUrl}${movie.genre}")
                    navController.navigate("ticketselection")
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(24.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C1A78)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Buy Ticket Now",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Poppins,
                color = Color.White
            )
        }
    }
}