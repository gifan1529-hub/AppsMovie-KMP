package com.example.moviekmp.Domain.Repository

import com.example.moviekmp.Domain.Model.User

interface UserRepository {

    suspend fun getUserByEmail(email: String): User?
    suspend fun updateUser(user: User)
    suspend fun findUserByEmail(email: String): User?
    suspend fun insertUser(user: User)

}