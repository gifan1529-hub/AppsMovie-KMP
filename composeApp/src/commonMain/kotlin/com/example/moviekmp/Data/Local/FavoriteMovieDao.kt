package com.example.moviekmp.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moviekmp.Domain.Model.FavoriteMovie
import com.example.moviekmp.Domain.Model.RoomApi
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    // untuk insert movie ke database
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMovie(movie: FavoriteMovie)

    // get all movie untuk ditampilin sesuai email
    @Query("SELECT * FROM favorite_movies_table WHERE email = :email")
    fun getAllFavoriteMovies(email: String): Flow<List<FavoriteMovie>>

    // nge delete movie dari database
    @Query("DELETE FROM favorite_movies_table WHERE id = :movieId AND email = :email")
    suspend fun removeFromFavorite(movieId: String, email: String)

    // cek apakah movie ada di database
    @Query("SELECT EXISTS (SELECT 1 FROM favorite_movies_table WHERE id = :movieId AND email = :email)")
    suspend fun isMovieFavorite(movieId: String, email: String): Boolean
    @Query("UPDATE movies SET isFavorite = :isFav WHERE id = :movieId")
    suspend fun updateFavoriteStatus(movieId: String, isFav: Boolean)

    @Update
    suspend fun updateMovie(movie: RoomApi)
}