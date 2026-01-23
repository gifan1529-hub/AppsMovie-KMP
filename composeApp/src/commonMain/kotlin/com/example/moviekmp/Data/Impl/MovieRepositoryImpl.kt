package com.example.moviekmp.Data.Impl

import com.example.moviekmp.Data.Local.FavoriteMovieDao
import com.example.moviekmp.Data.Local.RoomDao
import com.example.moviekmp.Data.Remote.ApiService
import com.example.moviekmp.Domain.Model.FavoriteMovie
import com.example.moviekmp.Domain.Model.RoomApi
import com.example.moviekmp.Domain.Repository.MovieRepository
import kotlinx.coroutines.flow.Flow

/**
 * implementasi dari [MovieRepository]
 */
class MovieRepositoryImpl (
    private val apiService: ApiService,
    private val movieDao: RoomDao,
    private val dao: FavoriteMovieDao,
) : MovieRepository {
    /**
     * nnyari daftar film dberdasarkan judul dari database
     */
    override suspend fun searchMovies(query: String): List<RoomApi> {
        return movieDao.searchMoviesByTitle("%$query%")
    }

    /**
     * ngambil daftar film dari database
     */
    override fun getMoviesFromLocal(): Flow<List<RoomApi>> {
        return movieDao.getAllMovies()
    }

    /**
     * ngambil daftar film dari api
     * dan memasukkannya ke database
     * biar bisa nampilin data film maskipun gada koneksi
     */
    override suspend fun refreshMovies() {
        try {
            val response = apiService.getTitles()

            val  moviesToInsert = response.results.map { movie ->
                println("qwerty: Film ${movie.titleText} poster: ${movie.primaryImage?.url}")
                RoomApi(
                    id = movie.id,
                    title = movie.titleText,
                    posterUrl = movie.primaryImage?.url,
                    type = movie.titleType,
                    genre = movie.genres?.joinToString(", "),
                    releaseYear = movie.releaseYear,
                    rating = movie.rating?.aggregateRating,
                    plot = movie.plot
                )
            }
            println("qwerty: Jumlah film dari API: ${moviesToInsert.size}")
            movieDao.insertAll(moviesToInsert)
        } catch (e: Exception) {
            println("qwerty: Error fetch API: ${e.message}")
        }
    }

    /**
     * ngambil daftar film favorit dari database
     */
    override fun getFavoriteMoviesFromLocal(): Flow<List<RoomApi>> {
        return movieDao.getFavoriteMovies()
    }

    /**
     * ngambil daftar film dari database sesuai id nya
     */
    override suspend fun getMovieByIdFromLocal(movieId: String): RoomApi? {
        return movieDao.getMovieById(movieId)
    }

    /**
     * update data film ke database
     */
    override suspend fun updateFavoriteStatus(movieId: String, isFavorite: Boolean) {
        movieDao.updateFavoriteStatus(movieId, isFavorite)
    }

    /**
     * update data film ke database
     */
    override suspend fun updateMovie(movie: RoomApi) {
        dao.updateMovie(movie)
    }

    /**
     * cek apakah data film di database isFavorite nya true
     */
    override suspend fun isMovieFavorite(movieId: String): Boolean {
        return movieDao.getMovieById(movieId)?.isFavorite == true
    }
    /**
     * ngubah data favorite movie dari database
     * isFavorite di [RoomApi] nya jadi true
     */
    override suspend fun addFavorite(movie: RoomApi) {
        val favoritedMovie = movie.copy(isFavorite = true)
        movieDao.insertAll(listOf(favoritedMovie))
    }

    /**
     * ngehapus data favorite movie dari database
     * isFavorite di [RoomApi] nya jadi false
     */
    override suspend fun removeFavorite(movie: RoomApi) {
        val unfavoritedMovie = movie.copy(isFavorite = false)
        movieDao.insertAll(listOf(unfavoritedMovie))
    }

    /**
     * masukin daftar favorite movie ke database
     */
    override suspend fun addMovieToFavorites(movie: FavoriteMovie) {
        dao.insertMovie(movie)
    }

    /**
     * ngehapus daftar favorite movie dari database
     */
    override suspend fun removeMovieFromFavorites(movie: FavoriteMovie) {
        return dao.removeFromFavorite(movie.id, email = movie.email)
    }

    /**
     * ngambil daftar favorite movie dari database
     */
    override fun getFavoriteMovies(email: String): Flow<List<FavoriteMovie>> {
        return dao.getAllFavoriteMovies(email)
    }

    /**
     * cek apakah favorite movie ada di database
     */
    override suspend fun isMovieFavorites(movieId: String, email: String): Boolean {
        return dao.isMovieFavorite(movieId, email)
    }
}