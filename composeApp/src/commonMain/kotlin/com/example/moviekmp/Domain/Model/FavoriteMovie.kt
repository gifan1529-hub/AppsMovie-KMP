package com.example.moviekmp.Domain.Model

import androidx.room.Entity

@Entity(tableName = "favorite_movies_table",
    primaryKeys = ["id", "email"] )
data class FavoriteMovie (
    val id: String,
    val email: String,
    val title: String?,
    val posterUrl: String?,
    val plot: String?,
    val rating: Double?
)