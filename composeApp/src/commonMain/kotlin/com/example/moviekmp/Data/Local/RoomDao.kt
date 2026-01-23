package com.example.moviekmp.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviekmp.Domain.Model.RoomApi
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    // untuk insert movie dari api ke databse
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<RoomApi>)

    // get all movie di database
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<RoomApi>>

    // nge delete semua movie di database
    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()

    // nge get movie berdasarkan judul
    @Query("SELECT * FROM movies WHERE title LIKE :query")
    suspend fun searchMoviesByTitle(query: String): List<RoomApi>

    // nge get movie berdasarkan id
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: String): RoomApi?

    // nge get movie yang favorite
    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getFavoriteMovies(): Flow<List<RoomApi>>

    // nge update favorite status dari movie
    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun updateFavoriteStatus(movieId: String, isFavorite: Boolean)

    @Query("SELECT isFavorite FROM movies WHERE id = :movieId")
    suspend fun isMovieFavorite(movieId: String): Boolean

}