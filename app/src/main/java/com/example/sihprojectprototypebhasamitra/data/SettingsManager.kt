package com.example.sihprojectprototypebhasamitra.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create the DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val FROM_LANG_KEY = stringPreferencesKey("from_lang")
        val TO_LANG_KEY = stringPreferencesKey("to_lang")
    }

    // Flow to get the 'from' language
    val fromLangFlow: Flow<String> = dataStore.data
        .map {
            // Default to Hindi if not set
            it[FROM_LANG_KEY] ?: "Hindi"
        }

    // Flow to get the 'to' language
    val toLangFlow: Flow<String> = dataStore.data
        .map {
            // Default to English if not set
            it[TO_LANG_KEY] ?: "English"
        }

    // Function to update the 'from' language
    suspend fun setFromLang(language: String) {
        dataStore.edit {
            it[FROM_LANG_KEY] = language
        }
    }

    // Function to update the 'to' language
    suspend fun setToLang(language: String) {
        dataStore.edit {
            it[TO_LANG_KEY] = language
        }
    }
}
