package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Domain.Model.RoomApi
import com.example.moviekmp.Domain.Usecase.GetMovieUC
import com.example.moviekmp.Domain.Usecase.RefreshMovieUC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class MovieRepositoryVM (
    private val getMovieUC: GetMovieUC,
    private val refreshMovieUC: RefreshMovieUC
) : ViewModel() {
    val allMovies: Flow<List<RoomApi>> = getMovieUC()

    init {
        refreshMovies()
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            refreshMovieUC()
        }
    }
}