package com.example.moviekmp.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.moviekmp.Domain.Model.User

@Dao
interface UserDao {
    // nge insert data user ke database
    @Insert
    suspend fun insertdata(user: User)

    // nge get data user dari database berdasarkan email
    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): User?

    // nge update data user ke database
    @Update
    suspend fun updateUser(user: User)

    // nge get data user dari database berdasarkan email
    @Query("SELECT * FROM user WHERE email = :userEmail LIMIT 1")
    suspend fun get(userEmail: String): User?
}