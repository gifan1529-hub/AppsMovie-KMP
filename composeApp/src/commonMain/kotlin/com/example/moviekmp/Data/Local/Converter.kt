package com.example.moviekmp.Data.Local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * untuk convert list ke string
 */
class Converter {
    /**
     * ngubah list menjadi string supaya bisa di simpan di database
     */
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    /**
     * ngubah string menjadi list supaya bisa di ambil dari database
     */
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}