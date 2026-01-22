package com.example.moviekmp.UI.Screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily
import com.example.moviekmp.ViewModel.BookingTicketVM
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SeatSelectionScreen(
    navController: NavController,
    viewModel: BookingTicketVM = koinViewModel()
) {
    val scrollState = rememberScrollState()
    val Poppins = getPoppinsFontFamily()

    val bookingData by viewModel.bookingData.collectAsState()

    var adultCount by remember { mutableIntStateOf(0) }
    var childCount by remember { mutableIntStateOf(0) }

    val selectedSeats = bookingData.selectedSeats
    val totalSeats = 80

    val darkBackground = Color(0xFF121212)
    val graySeat = Color(0xFF636363)
    val selectedColor = Color(0xFFBB86FC)
    Box() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Screen",
                color = Color.White,
                fontFamily = Poppins,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(15.dp))
            Box(modifier = Modifier.height(300.dp)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(10),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(totalSeats) { index ->
                        val isSelected = selectedSeats.contains(index)
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) selectedColor else graySeat)
                                .clickable {
                                    viewModel.onSeatSelected(index)
                                }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ticket Details", color = Color.White, fontFamily = Poppins)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    CounterItem(
                        label = "ADULT",
                        count = bookingData?.adultTickets ?: 0,
                        onCountChange = {  },
                        onIncrease = { viewModel.addAdultTicket() },
                        onDecrease = { viewModel.removeAdultTicket() }
                    )
                    CounterItem(
                        label = "CHILD",
                        count = bookingData?.childTickets ?: 0,
                        onCountChange = { },
                        onIncrease = { viewModel.addChildTicket() },
                        onDecrease = { viewModel.removeChildTicket() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Color.Gray,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement
                            .spacedBy(4.dp),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        SummaryText("Nama Film", bookingData?.movieTitle ?: "-")
                        val totalQuantity = (bookingData?.adultTickets ?: 0) + (bookingData?.childTickets ?: 0)
                        SummaryText("Quantity ticket", "$totalQuantity")
                        SummaryText("Session", bookingData?.session ?: "-")
                        SummaryText("Theater",bookingData?.theater ?: "-")
                        SummaryText("Buffet", bookingData?.selectedBuffet ?: "-")
                        SummaryText("Seat Number", selectedSeats.joinToString { "${it + 1}" })
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "Total Amount",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        if ((bookingData?.buffetSubtotal ?: 0.0) > 0) {
                            Text(
                                text = "Buffet: $ ${bookingData?.buffetSubtotal}",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "$ ${bookingData?.totalPrice ?: 0.0}",
                            color = Color.Green,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CounterItem(
    label: String,
    count: Int,
    onCountChange: (Int) -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = (onDecrease)
        ) {
            Icon(painterResource(
                Res.drawable.min),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(25.dp)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$count", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.Gray, fontSize = 10.sp)
        }
        IconButton(
            onClick = (onIncrease)
        ) {
            Icon(painterResource(
                Res.drawable.add),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(25.dp)
            )
        }
    }
}

@Composable
fun SummaryText(label: String, value: String) {
    val Poppins = getPoppinsFontFamily()
    Column {
        Text(
            label,
            color = Color.Gray,
            fontSize = 14.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal
        )

        Text(
            value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins
        )
    }
}

@Composable
fun Text(text: String) {
    val Poppins = getPoppinsFontFamily()
    Text(
        text,
        color = Color.Gray,
        fontSize = 14.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal
    )
}