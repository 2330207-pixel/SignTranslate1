package com.example.signtranslate.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

lateinit var appContext: Context

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_session"
)

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    return appContext.dataStore
}