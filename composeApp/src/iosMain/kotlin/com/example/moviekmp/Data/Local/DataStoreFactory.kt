
package com.example.moviekmp.Data.Local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import okio.Path.Companion.toPath


@OptIn(ExperimentalForeignApi::class)
actual fun createDataStore(context: Any): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath (
        produceFile = {
            val directory = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = true,
                error = null
            )
            val path = directory?.path ?: ""
            "$path/$DATASTORE_FILE_NAME".toPath()
        }
    )
}