package com.example.moviekmp.Data.Remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * buat nge get URL dari Api
 */
object ApiClient {

    private const val BASE_URL = "https://api.imdbapi.dev/"

    val client = HttpClient {
        install(Logging) {
            level = LogLevel.BODY
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    fun getUrl(endpoint: String) = "$BASE_URL$endpoint"
}