package com.example.moviekmp.Data.Remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * buat ngambil data dari api
 * data apa yang pengen di ambil dari database
 */
@Serializable
data class UserApiResponse (

    @SerialName("titles")
    val results: List<MovieResult>
)
@Serializable
data class MovieResult (

    @SerialName("id")
    val id: String,

    @SerialName("primaryImage")
    val primaryImage: PrimaryImage? = null,

    @SerialName("type")
    val titleType: String? = null,

    @SerialName("primaryTitle")
    val titleText: String? = null,

    @SerialName("originalTitle")
    val originalTitle: String? = null,

    @SerialName("startYear")
    val releaseYear: Int? = null,

    @SerialName("plot")
    val plot: String? = null,

    @SerialName("genres")
    val genres: List<String>? = null,

    @SerialName("rating")
    val rating: lating? = null
)
@Serializable
data class PrimaryImage (

    @SerialName("id")
    val id: String? = null,

    @SerialName("url")
    val url: String? = null,

    @SerialName("caption")
    val caption: Caption? = null
)
@Serializable
data class Caption(
    @SerialName("plainText")
    val plainText: String? = null
)

@Serializable
data class lating(
    @SerialName("aggregateRating")
    val aggregateRating: Double?,

    @SerialName("voteCount")
    val voteCount: Int?
)