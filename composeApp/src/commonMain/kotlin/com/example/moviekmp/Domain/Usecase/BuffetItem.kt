package com.example.moviekmp.Domain.Usecase

data class BuffetItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    var quantity: Int = 0
)