package com.example.moviekmp.DI

import android.widget.Toast
import com.example.moviekmp.Data.Local.MyMovieApp

actual class ToastHelper {
    actual fun showToast(message: String) {
        Toast.makeText(MyMovieApp.appContext, message, Toast.LENGTH_SHORT).show()
    }
}