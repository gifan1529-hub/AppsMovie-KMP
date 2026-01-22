package com.example.moviekmp.Data.Local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

const val DATASTORE_FILE_NAME = "user_prefs.preferences_pb"

expect fun createDataStore(context: Any): DataStore<Preferences>