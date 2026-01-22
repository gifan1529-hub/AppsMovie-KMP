package com.example.moviekmp.Data.Impl

import com.example.moviekmp.Data.Local.UserDao
import com.example.moviekmp.Domain.Model.User
import com.example.moviekmp.Domain.Repository.UserRepository

class  UserRepositoryImpl (
    private val userDao: UserDao
): UserRepository {
    override suspend fun getUserByEmail(email: String): User? {
        return userDao.get(email)
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    override suspend fun findUserByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    override suspend fun insertUser(user: User) {
        userDao.insertdata(user)
    }
}