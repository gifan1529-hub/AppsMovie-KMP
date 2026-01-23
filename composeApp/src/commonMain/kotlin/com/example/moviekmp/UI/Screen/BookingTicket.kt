package com.example.moviekmp.UI.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moviekmp.DI.ToastHelper
import com.example.moviekmp.Data.Local.PrefsManager
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily
import com.example.moviekmp.ViewModel.BookingTicketVM
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookingTicket(
    navController: NavController,
    viewModel: BookingTicketVM = koinViewModel(),
    prefsManager: PrefsManager
) {
    var currentStep by remember { mutableStateOf(1) }
    val bookingVM: BookingTicketVM = koinViewModel()
    var selectedPaymentMethod by remember { mutableStateOf("Bank Transfer") }
    val bookingData by bookingVM.bookingData.collectAsState()
    val scope = rememberCoroutineScope()
    var userEmail by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        prefsManager.getUserEmail().collect { email ->
            if (email != null) {
                userEmail = email
            }
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            BookingHeader(
                currentStep = currentStep,
                onBackClick = {
                    if (currentStep > 1) {
                        currentStep--
                    } else {
                        navController.popBackStack()
                        viewModel.resetBookingData()
                    }
                }
            )
        },
        bottomBar = {
            BookingFooter(
                currentStep = currentStep,
                onNextClick = {
                    when (currentStep) {
                        1 -> {
                            val isTheaterSelected = !bookingData?.theater.isNullOrEmpty()
                            val isSessionSelected = !bookingData?.session.isNullOrEmpty()

                            if (isTheaterSelected && isSessionSelected) {
                                errorMessage = null
                                currentStep++
                            } else {
                                ToastHelper().showToast("Harap pilih Theater dan Sesi")
                            }
                        }
                        2 -> {
                            val totalTickets = (bookingData?.adultTickets ?: 0) + (bookingData?.childTickets ?: 0)
                            val selectedSeatsCount = bookingData?.selectedSeats?.size ?: 0

                            if (totalTickets > 0 && selectedSeatsCount == totalTickets) {
                                currentStep++
                            } else if (totalTickets == 0) {
                                ToastHelper().showToast("Harap tambahkan jumlah tiket")
                            } else {
                                ToastHelper().showToast("Harap pilih $totalTickets kursi")
                            }
                        }
                        3 -> {
                            bookingVM.confirmPaymentAndSave(
                                method = selectedPaymentMethod,
                                userEmail = userEmail
                            )
                            println("email ${userEmail}")
                            navController.navigate("resultpayment") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
    ) { _ ->
        val pagerState = rememberPagerState(pageCount = { 3 })
        LaunchedEffect(currentStep) {
            pagerState.animateScrollToPage(currentStep - 1)
        }
        LaunchedEffect(pagerState.currentPage) {
            currentStep = pagerState.currentPage + 1
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize(),
            userScrollEnabled = false // biar bisa scroll kanan kiri manula
        ) { page ->
            when (page) {
                0 -> TicketSelectionScreen(
                    navController = navController,
                    viewModel = bookingVM
                )
                1 -> SeatSelectionScreen(navController = navController)
                2 -> PaymentScreen(
                    navController = navController,
                    viewModel = bookingVM,
                    onOptionSelected = { selectedPaymentMethod = it }
                )
            }
        }
    }
}

/**
 * ui untuk header booking
 */
@Composable
fun BookingHeader(currentStep: Int, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(40.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .alpha(0.5f)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.3f))
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.back),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..3) {
                StepCircle(
                    stepNumber = i,
                    isActive = i == currentStep
                )
                if (i < 3) Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Spacer(modifier = Modifier.size(35.dp))
    }
}

/**
 * ui untuk circle step
 */
@Composable
fun StepCircle(stepNumber: Int, isActive: Boolean) {
    val Poppins = getPoppinsFontFamily()
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(if (isActive) Color(0xFF3C1A78) else Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stepNumber.toString(),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Poppins
        )
    }
}

/**
 * ui untuk tombol booking footer
 */
@Composable
fun BookingFooter(currentStep: Int, onNextClick: () -> Unit) {
    val Poppins = getPoppinsFontFamily()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .navigationBarsPadding() // biar ga tertutup tombol navigasi
    ) {
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C1A78)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (currentStep < 3) "Next" else "Confirm Payment",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(Res.drawable.next),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}