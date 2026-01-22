package com.example.moviekmp.UI.Screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun PaymentScreen(
    navController: NavController,
    viewModel: BookingTicketVM = koinViewModel(),
    onOptionSelected: (String) -> Unit
) {
    var selectedOption by remember { mutableStateOf("Bank Transfer") }
    val bookingData by viewModel.bookingData.collectAsState()
    val Poppins = getPoppinsFontFamily()

    LaunchedEffect(selectedOption) {
        onOptionSelected(selectedOption)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Select Payment Options",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            PaymentOptionItem(
                title = "Bank Transfer",
                isSelected = selectedOption == "Bank Transfer",
                onClick = { selectedOption = "Bank Transfer" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PaymentOptionItem(
                title = "Cash",
                isSelected = selectedOption == "Cash",
                onClick = { selectedOption = "Cash" }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Amount",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "$ ${bookingData.totalPrice}",
                    color = Color(0xFF00FF00),
                    fontSize = 30.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PaymentOptionItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val Poppins = getPoppinsFontFamily()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF1E1E1E), RoundedCornerShape(16.dp))
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) Color(0xFF7E57C2) else Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium
            )

            Icon(
                painter = painterResource(
                    if (isSelected) Res.drawable.checked else Res.drawable.unchecked
                ),
                contentDescription = null,
                tint = if (isSelected) Color(0xFF7E57C2) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}