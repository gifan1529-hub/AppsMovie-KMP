package com.example.moviekmp.DI

import io.ktor.client.engine.HttpClientEngine
import org.koin.dsl.module
import io.ktor.client.engine.okhttp.OkHttp

actual val  platformModule = module {
    single <HttpClientEngine> { OkHttp.create() }
}