package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Domain.Model.RoomApi
import com.example.moviekmp.Domain.Usecase.GetMovieUC
import com.example.moviekmp.Domain.Usecase.RefreshMovieUC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * viewmodel dari homeScreen
 */
class MovieRepositoryVM (
    private val getMovieUC: GetMovieUC,
    private val refreshMovieUC: RefreshMovieUC
) : ViewModel() {
    /**
     * nampilin semua film dari database RoomApi
     */
    val allMovies: Flow<List<RoomApi>> = getMovieUC()

    init {
        refreshMovies()
    }

    /**
     * refresh data dari api ke database room
     */
    private fun refreshMovies() {
        viewModelScope.launch {
            refreshMovieUC()
        }
    }
}