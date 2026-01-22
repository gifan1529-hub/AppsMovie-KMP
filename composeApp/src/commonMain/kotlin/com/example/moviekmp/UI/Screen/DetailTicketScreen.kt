package com.example.moviekmp.UI.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coil3.request.ImageRequest
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily
import com.example.moviekmp.ViewModel.DetailTicketVM
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTicketScreen(
    navController: NavController,
    bookingId: Int,
    viewModel: DetailTicketVM = koinViewModel()
) {
    val Poppins = getPoppinsFontFamily()
//    val context = LocalContext.current
//    val view = androidx.compose.ui.platform.LocalView.current // Mengambil view root Compose

    LaunchedEffect(bookingId) {
        viewModel.loadBookingDetails(bookingId)
    }
    val ticket by viewModel.ticketDetails.collectAsState()


    Scaffold(
        containerColor = Color.Black,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
                title = {
                    Text("My Ticket", color = Color.White, fontFamily = Poppins, fontWeight = FontWeight.Medium)
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .size(35.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.downloadPdf(ticket)
//                        val name = "Ticket_${ticket?.movieTitle ?: "Movie"}"
//                        exportToPdf.exportViewToPdf(context, view, name)
                    }) {
                        Icon(painterResource(Res.drawable.donglot), contentDescription = null, tint = Color.Red, modifier = Modifier.size(24.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
//                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .width(250.dp)
                    .height(400.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ticket?.moviePosterUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.bakgron),
                    error = painterResource(Res.drawable.bakgron)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            DetailItem(label = "Movie", value = ticket?.movieTitle ?: "-")
            DetailItem(label = "Ticket", value = "${ticket?.adultTickets ?: 0} Adult, ${ticket?.childTickets ?: 0} Child")
            Text(
                text = ticket?.session ?: "-",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Text(
                text = ticket?.seatIds?.joinToString(", ") ?: "-",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(top = 0.dp)
            )
            Text(
                text = ticket?.buffetItems ?: "None",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(top = 0.dp)
            )
            Text(
                text = ticket?.theater ?: "",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(top = 0.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 0.dp)) {
        val Poppins = getPoppinsFontFamily()
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium
        )
    }
}