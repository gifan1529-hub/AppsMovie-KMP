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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moviekmp.Domain.Usecase.SearchState
import com.example.moviekmp.UI.Component.MovieCardSearch
import com.example.moviekmp.UI.Theme.getPoppinsFontFamily
import com.example.moviekmp.ViewModel.SearchVM
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    navController : NavController,
    onMovieClick: (String) -> Unit,
    viewModel: SearchVM = koinViewModel(),

    ) {
    val query by viewModel.searchQuery.collectAsState()
    val resultsState by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val Poppins = getPoppinsFontFamily()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(Res.drawable.buled),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(Res.drawable.buledkiri),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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
                        .clickable { navController.navigate("home") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )

                OutlinedTextField(
                    value = query,
                    onValueChange = { viewModel.onQueryChange(it) },
                    trailingIcon = {
                        if (isSearching) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    placeholder = {
                        Text(
                            text = "Judul.....",
                            color = Color.Gray,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
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
                Spacer(modifier = Modifier.width(45.dp))
            }
            Spacer(modifier = Modifier.height(40.dp))

            when (val state = resultsState) {
                is SearchState.Loading -> {
                    CircularProgressIndicator()
                }

                is SearchState.Success -> {
                    val movies = state.movies
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(movies) { movie ->
                            MovieCardSearch(
                                movie = movie,
                                onClick = { navController.navigate("detailfilm/${movie.id}") }
                            )
                        }
                    }
                }

                is SearchState.Error -> {
                    Text(text = "Error: ${state.message}")
                }

                is SearchState.EmptyQuery -> {
                    Text(text = "Cari film favoritmu!")
                }

                else -> {}
            }
        }
    }
}