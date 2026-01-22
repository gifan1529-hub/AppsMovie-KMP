package com.example.moviekmp.UI.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily
import com.example.moviekmp.ViewModel.EditUserUiState
import com.example.moviekmp.ViewModel.EditUserVM
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditUserScreen(
    navController : NavController,
    userEmail: String,
    onEditSucces: (String) -> Unit,
    viewModel: EditUserVM = koinViewModel()
) {
    LaunchedEffect(userEmail) {
        viewModel.loadUser(userEmail)
    }
    val Poppins = getPoppinsFontFamily()
    val uiState by viewModel.uiState.collectAsState(EditUserUiState())

    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("refresh_trigger", true) // ngirim surat yang isinya true

            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { navController.navigate("user") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Edit User",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.width(45.dp))
            }
            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(Res.drawable.propil),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier
                .height(40.dp)
            )

            Text(
                text = "Email",
                color = Color.White,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = {viewModel.onEmailChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                placeholder = { Text(
                    text = "Email.....",
                    color = Color.Gray,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal
                ) },
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3C1A78), // Border Ungu pas ada tulisan
                    unfocusedBorderColor = Color(0xFFffffff), // Border Ungu pas diem
                    focusedContainerColor = Color(0xFF1A1A1A), // Warna dalem abu-abu gelap
                    unfocusedContainerColor = Color(0xFF1A1A1A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Password",
                color = Color.White,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = uiState.passwordBaru,
                onValueChange = { viewModel.onPasswordBaruChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                placeholder = { Text(
                    text = "Password.....",
                    color = Color.Gray,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal
                ) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3C1A78), // Border Ungu pas ada tulisan
                    unfocusedBorderColor = Color(0xFFffffff), // Border Ungu pas diem
                    focusedContainerColor = Color(0xFF1A1A1A), // Warna dalem abu-abu gelap
                    unfocusedContainerColor = Color(0xFF1A1A1A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.onUpdateClicked() },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C1A78)),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Save Change",
                    fontFamily = Poppins,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(43.dp))
        }
    }
}