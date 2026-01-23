package com.example.moviekmp.Data.Local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviekmp.Domain.Model.FavoriteMovie
import com.example.moviekmp.Domain.Model.RoomApi
import com.example.moviekmp.Domain.Model.User
import com.example.moviekmp.Domain.Model.BookingHistory
import com.example.moviekmp.Data.Local.RoomDao
import com.example.moviekmp.Data.Local.UserDao

/**
 * daftar database dan dao yang akan digunakan
 */
@TypeConverters(Converter::class)
@Database(
    entities = [
        User::class,
        RoomApi::class,
        FavoriteMovie::class,
        BookingHistory::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun roomDao() : RoomDao
    abstract fun movieDao() : FavoriteMovieDao
    abstract fun bookingHistoryDao(): BookingHistoryDao
}