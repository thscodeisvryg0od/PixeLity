package com.example.pixellauncher.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "launcher_prefs")

@Singleton
class LauncherPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val DOCK_APPS = stringSetPreferencesKey("dock_apps")
        val HIDDEN_APPS = stringSetPreferencesKey("hidden_apps")
    }

    /** Ordered list of package/activity keys currently pinned to the dock. */
    val dockApps: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[Keys.DOCK_APPS] ?: emptySet()
    }

    val hiddenApps: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[Keys.HIDDEN_APPS] ?: emptySet()
    }

    suspend fun setDockApps(keys: Set<String>) {
        context.dataStore.edit { it[Keys.DOCK_APPS] = keys }
    }

    suspend fun addToDock(key: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.DOCK_APPS] ?: emptySet()
            prefs[Keys.DOCK_APPS] = current + key
        }
    }

    suspend fun removeFromDock(key: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.DOCK_APPS] ?: emptySet()
            prefs[Keys.DOCK_APPS] = current - key
        }
    }

    suspend fun hideApp(key: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.HIDDEN_APPS] ?: emptySet()
            prefs[Keys.HIDDEN_APPS] = current + key
        }
    }

    suspend fun unhideApp(key: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.HIDDEN_APPS] ?: emptySet()
            prefs[Keys.HIDDEN_APPS] = current - key
        }
    }
}
