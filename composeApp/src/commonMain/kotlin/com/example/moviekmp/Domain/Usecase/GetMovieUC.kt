package com.example.moviekmp.Domain.Usecase

import com.example.moviekmp.Domain.Model.RoomApi
import com.example.moviekmp.Domain.Repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovieUC  (
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<RoomApi>> {
        return repository.getMoviesFromLocal()
    }
}