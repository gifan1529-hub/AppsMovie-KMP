package com.example.moviekmp.UI.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.moviekmp.DI.ToastHelper
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily
import com.example.moviekmp.ViewModel.BookingTicketVM
import kotlinx.coroutines.launch
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TicketSelectionScreen(
    navController: NavController,
    viewModel: BookingTicketVM = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val bookingData by viewModel.bookingData.collectAsState()
    val Poppins = getPoppinsFontFamily()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(bookingData) {
//        Log.d("TicketSelection", "Data Diterima di UI: " +
//                "Title: ${bookingData.movieTitle}, " +
//                "Poster: ${bookingData.moviePosterUrl}")
    }

    var showTheaterDialog by remember { mutableStateOf(false) }
    var selectedTheater by remember { mutableStateOf("Choose a Movie Theater *") }
    val theaterList = listOf("Cinema XXI - Plaza Senayan", "CGV - Grand Indonesia", "Cinépolis - Pejaten Village")

    var showSessionDialog by remember { mutableStateOf(false) }
    var selectedSession by remember { mutableStateOf("Select Session *") }
    val sessionList = listOf("10:00 AM", "13:00 PM", "16:00 PM", "19:00 PM")

    val darkBackground = Color(0xFF121212)
    val cardBackground = Color(0xFF1E1E1E)
    val purpleBorder = Color(0xFF3C1A78)
    val grayText = Color(0xFF9E9E9E)

    val isSelectionComplete = !bookingData?.theater.isNullOrEmpty() && !bookingData?.session.isNullOrEmpty()

    if (showTheaterDialog){
        AlertDialog(
            onDismissRequest = {showTheaterDialog = false},
            containerColor = Color(0xFF2C2C2E),
            shape = RoundedCornerShape(28.dp),
            confirmButton = {},
            title = {
                Text(
                    text = "Pilih Theater",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium
                )
            },
            text = {
                Column {
                    theaterList.forEach { theater ->
                        Text(
                            text = theater,
                            color = Color.White,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setTheater(theater)
                                    showTheaterDialog = false
                                }
                                .padding(vertical = 12.dp),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        )
    }

    if (showSessionDialog){
        AlertDialog(
            onDismissRequest = {showSessionDialog = false},
            containerColor = Color(0xFF2C2C2E),
            shape = RoundedCornerShape(28.dp),
            confirmButton = {},
            title = {
                Text (
                    text = "Pilih Session",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium
                )
            },
            text = {
                Column {
                    sessionList.forEach { session ->
                        Text(
                            text = session,
                            color = Color.White,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setSession(session)
                                    showSessionDialog = false
                                }
                                .padding(vertical = 12.dp),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        )
    }
    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { _ ->
        Box() {
            Image(
                painter = painterResource(Res.drawable.bakgron),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(cardBackground)

                ) {
                    AsyncImage(
                        model = bookingData?.moviePosterUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        placeholder = painterResource(Res.drawable.bakgron),
                        error = painterResource(Res.drawable.bakgron)
                    )
                    println("qwerty : ${bookingData?.moviePosterUrl}")
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Text(
                            text = bookingData?.movieTitle ?: ".",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = bookingData?.movieGenre ?: ".",
                            color = grayText,
                            fontWeight = FontWeight.Normal,
                            fontFamily = Poppins,
                            fontSize = 14.sp
                        )
                    }
                    if (bookingData?.moviePosterUrl == null) {
                        Text(
                            text = "Poster URL is NULL",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "You need to select the mandatory fields (*) to proceed to the checkout page.",
                    color = if (isSelectionComplete) grayText else Color.Red,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                SelectionItem(
                    title = bookingData?.theater ?: "Choose a Movie Theater *",
                    borderColor = if (!bookingData?.theater.isNullOrEmpty()) purpleBorder else Color.Gray,
                    onClick = {
                        showTheaterDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SelectionItem(
                    title = bookingData?.session ?: "Select Session *",
                    borderColor = if (!bookingData?.session.isNullOrEmpty()) purpleBorder else Color.Gray,
                    onClick = {
                        if (!bookingData?.theater.isNullOrEmpty()) {
                            showSessionDialog = true
                        } else {
                            scope.launch {
                                ToastHelper().showToast("Mohon pilih theater terlebih dahulu")
//                    Toast.makeText(context,"Mohon pilih theater terlebih dahulu",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                SelectionItem(
                    title = "Buffet Products",
                    borderColor = if (isSelectionComplete) purpleBorder else Color.Gray,
                    onClick = {
                        if (isSelectionComplete) {
                            navController.navigate("buffet")
                        } else {
                            ToastHelper().showToast("Mohon pilih theater dan session terlebih dahulu")
                            errorMessage = "Mohon pilih theater dan session terlebih dahulu"
//                        Toast.makeText(context,"Mohon pilih theater dan session terlebih dahulu",Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun SelectionItem(
    title: String,
    borderColor: Color,
    onClick: () -> Unit
) {
    val Poppins = getPoppinsFontFamily()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontFamily = Poppins,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White
        )
    }
}