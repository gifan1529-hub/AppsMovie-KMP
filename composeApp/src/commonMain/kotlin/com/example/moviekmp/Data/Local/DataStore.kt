package com.example.moviekmp.Data.Local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PrefsManager(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_USER_ID = intPreferencesKey("user_id")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_EMAIL = stringPreferencesKey("key_email")
        private val KEY_PASSWORD = stringPreferencesKey("key_password")
    }

    suspend fun createLoginSession(id: Int, email: String, password: String) {
        dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = true
            prefs[KEY_USER_ID] = id
            prefs[KEY_EMAIL] = email
            prefs[KEY_PASSWORD] = password
        }
    }

    fun getUserEmail(): Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_EMAIL] ?: "Email Tidak Ditemukan"
    }

     suspend fun isLoggedIn(): Boolean {
        return dataStore.data.map { it[IS_LOGGED_IN] ?: false }.first()
    }

    suspend fun logoutUser() {
        dataStore.edit { it.clear() }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { it[KEY_EMAIL] = email }
    }

    suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        dataStore.edit { it[IS_LOGGED_IN] = isLoggedIn }
    }
}