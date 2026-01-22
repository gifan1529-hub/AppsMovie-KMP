package com.example.moviekmp.UI.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoginScreen(
    navController : NavHostController
) {
    val Poppins = getPoppinsFontFamily()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(Res.drawable.gambar),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Image(
                painter = painterResource(Res.drawable.movie),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 20.dp)
            )
            Button(
                onClick = {navController.navigate("signin") },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3C1A78)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(
                        height = 50.dp,
                        width = 20.dp
                    )
                    .padding(horizontal = 30.dp)
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                )
            }
            Text(
                text = "or",
                color = Color.White.copy(alpha = 0.7f),
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
            Button(
                onClick = {navController.navigate("signup") },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3C1A78)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(
                        height = 50.dp,
                        width = 20.dp
                    )
                    .padding(horizontal = 30.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                )
            }
            Spacer(
                modifier = Modifier
                    .height(200.dp)
            )
        }
    }
}