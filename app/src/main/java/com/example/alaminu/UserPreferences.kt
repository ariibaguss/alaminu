package com.example.alaminu

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.alaminu.ui.profil.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(context: Context) {
    private val dataStore = context.dataStore

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }

    suspend fun savePassword(password: String) {
        dataStore.edit { preferences ->
            preferences[PASSWORD_KEY] = password
        }
    }

    suspend fun saveUserData(userData: UserData) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userData.id
            preferences[USERNAME_KEY] = userData.username
            preferences[EMAIL_KEY] = userData.email
            preferences[NO_WA_KEY] = userData.no_wa
            preferences[JENIS_KELAMIN_KEY] = userData.jenis_kelamin
            // Add other properties as needed
        }
    }

    val usernameFlow = dataStore.data.map { preferences ->
        preferences[USERNAME_KEY] ?: ""
    }

    val passFlow = dataStore.data.map { preferences ->
        preferences[PASSWORD_KEY] ?: ""
    }

    val userIdFlow = dataStore.data.map { preferences ->
        preferences[USER_ID_KEY] ?: ""
    }

    val emailFlow = dataStore.data.map { preferences ->
        preferences[EMAIL_KEY] ?: ""
    }

    val noWaFlow = dataStore.data.map { preferences ->
        preferences[NO_WA_KEY] ?: ""
    }

    val jenisKelaminFlow = dataStore.data.map { preferences ->
        preferences[JENIS_KELAMIN_KEY] ?: ""
    }

    suspend fun getUsername(): String {
        return usernameFlow.first()
    }

    suspend fun getEmail(): String {
        return emailFlow.first()
    }

    suspend fun getNowa(): String {
        return noWaFlow.first()
    }

    suspend fun getJk(): String {
        return jenisKelaminFlow.first()
    }

    suspend fun getPasssword(): String {
        return passFlow.first()
    }

    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val USER_ID_KEY = stringPreferencesKey("id_pengguna")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NO_WA_KEY = stringPreferencesKey("no_wa")
        private val JENIS_KELAMIN_KEY = stringPreferencesKey("jenis_kelamin")
    }
}
