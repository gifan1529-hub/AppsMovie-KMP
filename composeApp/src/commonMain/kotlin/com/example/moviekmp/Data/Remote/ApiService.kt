package com.example.moviekmp.Data.Remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

//interface ApiService {
//    @GET("titles")
//    suspend fun getTitles(): UserApiResponse
//
//    @GET("titles/{id}")
//    suspend fun getMovieDetails(
//        @Path("id") movieId: String
//    ): MovieResult
//
//    @GET("titles")
//    suspend fun searchTitles(
//        @Query("primaryTitle") query: String
//    ): UserApiResponse
//
//}

class ApiService(private val client: HttpClient) {

    suspend fun getTitles(): UserApiResponse {
        return client.get(ApiClient.getUrl("titles")).body()
    }

    suspend fun getMovieDetails(movieId: String): MovieResult {
        // Ini pengganti @Path("id")
        return client.get(ApiClient.getUrl("titles/$movieId")).body()
    }

    suspend fun searchTitles(query: String): UserApiResponse {
        return client.get(ApiClient.getUrl("titles")) {
            // Ini pengganti @Query("primaryTitle")
            parameter("primaryTitle", query)
        }.body()
    }
}