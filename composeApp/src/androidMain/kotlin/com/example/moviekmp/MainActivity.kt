package com.example.moviekmp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
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
            val context = LocalContext.current
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    Toast.makeText(
                        context,
                        "Permission Granted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Permission Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            LaunchedEffect(Unit) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    // ngecek apakah sudah pernah diizinkan sebelumnya
                    val isAlreadyGranted = androidx.core.content.ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                    if (!isAlreadyGranted) {
                        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}