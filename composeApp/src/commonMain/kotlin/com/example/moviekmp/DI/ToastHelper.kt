package com.example.moviekmp.DI

/**
 * expect untuk Toast di android dan ios
 */
expect class ToastHelper() {
    fun  showToast (message: String)
}