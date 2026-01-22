package com.example.moviekmp.Data.Impl

import com.example.moviekmp.Data.Local.FavoriteMovieDao
import com.example.moviekmp.Data.Local.RoomDao
import com.example.moviekmp.Data.Remote.ApiService
import com.example.moviekmp.Domain.Model.FavoriteMovie
import com.example.moviekmp.Domain.Model.RoomApi
import com.example.moviekmp.Domain.Repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl (
    private val apiService: ApiService,
    private val movieDao: RoomDao,
    private val dao: FavoriteMovieDao,
) : MovieRepository {

    override suspend fun searchMovies(query: String): List<RoomApi> {
        return movieDao.searchMoviesByTitle("%$query%")
    }

    override fun getMoviesFromLocal(): Flow<List<RoomApi>> {
        return movieDao.getAllMovies()
    }

    //    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun refreshMovies() {
//        if (isOnline()) {
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
//                Log.e("MovieRepository", "Error refreshing movies: ${e.message}")
        }
//        } else {
////            Log.d("MovieRepository", "Not connected to the internet. Skipping refresh.")
//        }
    }

    override fun getFavoriteMoviesFromLocal(): Flow<List<RoomApi>> {
        return movieDao.getFavoriteMovies()
    }

    override suspend fun getMovieByIdFromLocal(movieId: String): RoomApi? {
        return movieDao.getMovieById(movieId)
    }

    override suspend fun updateFavoriteStatus(movieId: String, isFavorite: Boolean) {
        movieDao.updateFavoriteStatus(movieId, isFavorite)
    }

    override suspend fun updateMovie(movie: RoomApi) {
        dao.updateMovie(movie)
    }

    override suspend fun isMovieFavorite(movieId: String): Boolean {
        return movieDao.getMovieById(movieId)?.isFavorite == true
    }

    override suspend fun addFavorite(movie: RoomApi) {
        val favoritedMovie = movie.copy(isFavorite = true)
        movieDao.insertAll(listOf(favoritedMovie))
    }

    override suspend fun removeFavorite(movie: RoomApi) {
        val unfavoritedMovie = movie.copy(isFavorite = false)
        movieDao.insertAll(listOf(unfavoritedMovie))
    }

    override suspend fun addMovieToFavorites(movie: FavoriteMovie) {
        dao.insertMovie(movie)
    }

    override suspend fun removeMovieFromFavorites(movie: FavoriteMovie) {
        return dao.removeFromFavorite(movie.id, email = movie.email)
    }

    override fun getFavoriteMovies(email: String): Flow<List<FavoriteMovie>> {
        return dao.getAllFavoriteMovies(email)
    }

    override suspend fun isMovieFavorites(movieId: String, email: String): Boolean {
        return dao.isMovieFavorite(movieId, email)
    }

//    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
//    private fun isOnline(): Boolean {
//        // ngasih informasi tentang status jaringan hp
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        // cek apakah ada koneksi yang aktif
//        val network = connectivityManager.activeNetwork ?: return false
//        // cek kemampuan koneksi yang aktif
//        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
//        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//    }
}