package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Data.Local.FavoriteMovieDao
import com.example.moviekmp.Data.Local.PrefsManager
import com.example.moviekmp.Domain.Model.FavoriteMovie
import com.example.moviekmp.Domain.Model.RoomApi
import com.example.moviekmp.Domain.Repository.MovieRepository
import com.example.moviekmp.Domain.Usecase.DetailResult
import com.example.moviekmp.Domain.Usecase.GetMovieDetailUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailFilmVM (
    private val favoriteDao: FavoriteMovieDao,
    private val prefsManager: PrefsManager,
    private val repository: MovieRepository,
    private val getMovieDetailUC: GetMovieDetailUC
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailResult>(DetailResult.Loading)
    val uiState: StateFlow<DetailResult> = _uiState
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    /**
     * ngambil data detail film sesuai sama id nya
     */
    fun getMovieById(movieId: String) {
        viewModelScope.launch {
            val currentEmail = prefsManager.getUserEmail().first()
            val localStatus = favoriteDao.isMovieFavorite(movieId, currentEmail) ?: false
            _isFavorite.value = localStatus

            getMovieDetailUC.execute(movieId).collect { result ->
                if (result is DetailResult.Success) {
                    result.movie.isFavorite = localStatus
                }
                _uiState.value = result
            }
        }
    }

    /**
     * buat nambah atau hapus film dari favorite database
     * Alur: Ambil email user -> Cek status sekarang -> Simpan/Hapus dari favorite database
     */
    fun toggleFavoriteStatus(movie: RoomApi) {
        viewModelScope.launch {
            val newStatus = !_isFavorite.value
            val userEmail = prefsManager.getUserEmail().first()

            if (newStatus) {
                val favoriteMovie = FavoriteMovie(
                    id = movie.id,
                    email = userEmail,
                    title = movie.title,
                    posterUrl = movie.posterUrl,
                    plot = movie.plot,
                    rating = movie.rating
                )
                favoriteDao.insertMovie(favoriteMovie)
            } else {
                favoriteDao.removeFromFavorite(movie.id, userEmail)
            }

            _isFavorite.value = newStatus
        }
    }
}