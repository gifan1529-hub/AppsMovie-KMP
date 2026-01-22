package com.example.moviekmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform