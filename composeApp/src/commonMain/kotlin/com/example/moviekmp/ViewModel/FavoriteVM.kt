package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Data.Local.FavoriteMovieDao
import com.example.moviekmp.Data.Local.PrefsManager
import com.example.moviekmp.Domain.Model.FavoriteMovie
import com.example.moviekmp.Domain.Model.RoomApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class FavoriteVM (
    private val favoriteDao: FavoriteMovieDao,
    private val prefsManager: PrefsManager
) : ViewModel() {

    val favoriteMovies: StateFlow<List<FavoriteMovie>> =  prefsManager.getUserEmail()
        .flatMapLatest { email ->
            // gambil data favorit sesuai email yang dapet dari DataStore
            favoriteDao.getAllFavoriteMovies(email)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // itu biar si flow favorite nya ga aktif terus, jadi itu bakal aktif kalo si user cuman berada di favorite screen. kalo ga berada di favorite screen bakal ngitung selama 5 detik dan kalo selama 5 detik itu si user ga balik lagi ke favorite, flow nya bakal ketutup
            initialValue = emptyList()
        )

    fun removeFromFavorite(movie: RoomApi) {
        viewModelScope.launch {
            val updatedMovie = movie.copy(isFavorite = false)
            favoriteDao.updateFavoriteStatus(updatedMovie.id, false)
        }
    }
}