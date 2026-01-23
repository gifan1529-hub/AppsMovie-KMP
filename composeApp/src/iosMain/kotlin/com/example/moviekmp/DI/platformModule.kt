package com.example.moviekmp.DI

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

/**
 * ini buat engine nya pake darwin
 */
actual val platformModule = module {
    single <HttpClientEngine> { Darwin.create() }
}