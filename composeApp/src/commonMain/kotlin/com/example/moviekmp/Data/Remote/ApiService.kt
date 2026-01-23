package com.example.moviekmp.Data.Remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
class ApiService(private val client: HttpClient) {
    /**
     * ngambil data dari api
     */
    suspend fun getTitles(): UserApiResponse {
        return client.get(ApiClient.getUrl("titles")).body()
    }
}