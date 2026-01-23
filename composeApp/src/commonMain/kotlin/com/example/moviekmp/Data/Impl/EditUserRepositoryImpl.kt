package com.example.moviekmp.Data.Impl

import com.example.moviekmp.Data.Local.UserDao
import com.example.moviekmp.Domain.Model.User
import com.example.moviekmp.Domain.Repository.EditUserRepository

/**
 * implementasi dari [EditUserRepository]
 */
class EditUserRepositoryImpl (
    private val userDao: UserDao
) : EditUserRepository {
    /**
     * ngambil data user dari database sesuai sama email nya
     */
    override suspend fun getUserByEmail(email: String): User? {
        return userDao.get(email)
    }

    /**
     * update data user ke database
     */
    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}