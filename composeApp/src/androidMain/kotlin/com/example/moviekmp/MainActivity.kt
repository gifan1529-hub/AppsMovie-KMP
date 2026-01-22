package com.example.moviekmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.moviekmp.DI.appModule
import com.example.moviekmp.Data.Local.AppDatabase
import com.example.moviekmp.Data.Local.createDataStore
import com.example.moviekmp.UI.Navigation.App
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import androidx.datastore.preferences.core.Preferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(this@MainActivity.applicationContext
                )
                modules(
                    appModule,
                    module {
                        single<DataStore<Preferences>> { createDataStore(androidContext()) }
                        single<AppDatabase> {
                                val dbFile = androidContext().getDatabasePath("movie_db.db")
                                Room.databaseBuilder<AppDatabase>(
                                context = androidContext(),
                                name = dbFile.absolutePath
                            )
                                .setDriver(BundledSQLiteDriver())
                                .build()
                        }
                    }
                    )

            }
        }
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}