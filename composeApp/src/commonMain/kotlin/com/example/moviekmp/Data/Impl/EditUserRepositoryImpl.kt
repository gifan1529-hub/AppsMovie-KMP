package com.example.moviekmp.Data.Impl

import com.example.moviekmp.Data.Local.UserDao
import com.example.moviekmp.Domain.Model.User
import com.example.moviekmp.Domain.Repository.EditUserRepository

class EditUserRepositoryImpl (
    private val userDao: UserDao
) : EditUserRepository {
    override suspend fun getUserByEmail(email: String): User? {
        return userDao.get(email)
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}