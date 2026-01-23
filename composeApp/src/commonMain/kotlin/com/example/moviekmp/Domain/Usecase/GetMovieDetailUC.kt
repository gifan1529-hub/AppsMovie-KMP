package com.example.moviekmp.Domain.Usecase

import com.example.moviekmp.Domain.Model.RoomApi
import com.example.moviekmp.Domain.Repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class DetailResult {
    data class Success(val movie: RoomApi) : DetailResult()
    data class Error(val message: String) : DetailResult()
    object Loading : DetailResult()
}

/**
 * usecase untuk mengambil data detail film berdasarkan id
 */
class GetMovieDetailUC (
    private val repository: MovieRepository
) {
    fun execute(movieId: String): Flow<DetailResult> = flow {
        emit(DetailResult.Loading)

        try {
            val movieDetail: RoomApi? = repository.getMovieByIdFromLocal(movieId)
            if (movieDetail != null) {
                emit(DetailResult.Success(movieDetail))
            } else {
                emit(DetailResult.Error("Movie not found"))
            }
        } catch (e: Exception) {
            emit(DetailResult.Error(e.message ?: "Unknown error"))
        }
    }
}