package com.example.moviekmp.UI.Component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    selectedIndex: Int,
    onHomeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onTicketClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp, start = 40.dp, end = 40.dp)
            .height(90.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp),
            shape = RoundedCornerShape(25.dp),
            color = Color.Black.copy(alpha = 0.85f),
            shadowElevation = 15.dp,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 35.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    selected = selectedIndex == 0,
                    iconRes = Res.drawable.loves,
                    label = "Favorite",
                    onClick = onFavoriteClick
                )

                Spacer(modifier = Modifier.width(60.dp))

                BottomNavItem(
                    selected = selectedIndex == 2,
                    iconRes = Res.drawable.ticket,
                    label = "Ticket",
                    onClick = onTicketClick
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(68.dp)
                .clip(CircleShape),
            shape = CircleShape,
            color = Color.Transparent,
            shadowElevation = 12.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF8E66D1), Color(0xFF6A49A1))
                        )
                    )
                    .clickable {
                        println("Home Clicked")
                        onHomeClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.homep),
                    contentDescription = "Home",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    iconRes : DrawableResource,
    label: String,
    onClick: () -> Unit,
    selected: Boolean
) {
    val Poppins = getPoppinsFontFamily()

    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 8.sp,
            fontFamily = Poppins
        )
    }
}