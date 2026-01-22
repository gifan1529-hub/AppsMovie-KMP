package com.example.moviekmp.Domain.Usecase

import com.example.moviekmp.Domain.Repository.MovieRepository

class RefreshMovieUC (
    private val repository: MovieRepository
) {
    suspend operator fun invoke() {
        repository.refreshMovies()
    }
}