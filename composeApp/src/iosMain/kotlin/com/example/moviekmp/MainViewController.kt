package com.example.moviekmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.moviekmp.DI.initKoin
import com.example.moviekmp.UI.Navigation.App

fun MainViewController() = ComposeUIViewController (
    configure = {
        initKoin ()
    }
) {
    App()
}