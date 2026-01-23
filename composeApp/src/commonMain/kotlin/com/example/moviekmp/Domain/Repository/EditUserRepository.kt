package com.example.moviekmp.Domain.Repository

import com.example.moviekmp.Domain.Model.User

/**
 * interface dari [UserRepository]
 */
interface EditUserRepository {

    suspend fun getUserByEmail(email: String): User?
    suspend fun updateUser(user: User)

}