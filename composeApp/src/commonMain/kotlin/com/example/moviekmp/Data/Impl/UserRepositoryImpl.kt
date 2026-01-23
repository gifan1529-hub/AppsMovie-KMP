package com.example.moviekmp.Data.Impl

import com.example.moviekmp.Data.Local.UserDao
import com.example.moviekmp.Domain.Model.User
import com.example.moviekmp.Domain.Repository.UserRepository

/**
 * implementasi dari [UserRepository]
 */
class  UserRepositoryImpl (
    private val userDao: UserDao
): UserRepository {
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

    /**
     * nyari email di databasse
     */
    override suspend fun findUserByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    /**
     * masukin data user ke database
     */
    override suspend fun insertUser(user: User) {
        userDao.insertdata(user)
    }
}