package com.example.moviekmp.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviekmp.Domain.Model.BookingHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingHistoryDao {
    // untuk insert booking ke database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingHistory): Long

    // get all booking untuk ditampilin sesuai email
    @Query("SELECT * FROM booking_history WHERE email = :email ORDER BY bookingDate DESC")
    fun getBookingsByEmail(email: String): Flow<List<BookingHistory>>

    // get booking by id untuk ditampilin
    @Query("SELECT * FROM booking_history WHERE id = :id LIMIT 1")
    suspend fun getBookingById(id: Int): BookingHistory?

    // nge get seat yang udah di booking
    @Query("SELECT seatIds FROM booking_history WHERE movieTitle = :movieId AND theater = :theater AND session = :session")
    suspend fun getTakenSeatsForShow(movieId: String, theater: String, session: String): List<String>


}