package com.example.moviekmp.Domain.Repository

import com.example.moviekmp.Domain.Model.FavoriteMovie
import com.example.moviekmp.Domain.Model.RoomApi
import kotlinx.coroutines.flow.Flow

/**
 * interface dari [MovieRepository]
 */
interface MovieRepository  {
    suspend fun searchMovies(query: String): List<RoomApi>

    fun getMoviesFromLocal(): Flow<List<RoomApi>>
    suspend fun refreshMovies()

    fun getFavoriteMoviesFromLocal(): Flow<List<RoomApi>>

    suspend fun getMovieByIdFromLocal(movieId: String): RoomApi?
    suspend fun updateFavoriteStatus(movieId: String, isFavorite: Boolean)
    suspend fun updateMovie (movie: RoomApi)
    suspend fun isMovieFavorite(movieId: String): Boolean
    suspend fun addFavorite(movie: RoomApi)
    suspend fun removeFavorite(movie: RoomApi)

    suspend fun addMovieToFavorites(movie: FavoriteMovie)
    suspend fun removeMovieFromFavorites(movie: FavoriteMovie)
    fun getFavoriteMovies(email: String): Flow<List<FavoriteMovie>>
    suspend fun isMovieFavorites(movieId: String, email: String): Boolean

}