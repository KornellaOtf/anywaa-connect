import com.anywaa.connect.util.FeatureFlags
package com.anywaa.connect.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

class ThemePreferences(private val context: Context) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_mode")
        private val WEB_SEARCH_KEY = booleanPreferencesKey("web_search_enabled")
        private val LANGUAGE_KEY = stringPreferencesKey("app_language")
        private val EMBEDDING_ENABLED_KEY = booleanPreferencesKey("embedding_enabled")
        private val SELECTED_EMBEDDING_MODEL_KEY = stringPreferencesKey("selected_embedding_model")
        private val MEMORY_ENABLED_KEY = booleanPreferencesKey("memory_enabled")
        private val AUTO_READOUT_ENABLED_KEY = booleanPreferencesKey("auto_readout_enabled")
        private val IS_PREMIUM_KEY = booleanPreferencesKey("is_premium")
        private val GITHUB_STARS_KEY = androidx.datastore.preferences.core.intPreferencesKey("github_stars")
        private val ANYWAA_MODE_KEY = booleanPreferencesKey("anywaa_mode")
        private val LOW_DATA_MODE_KEY = booleanPreferencesKey("low_data_mode")
        private val SYSTEM_PROMPT_KEY = stringPreferencesKey("system_prompt")
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data
        .map { preferences ->
            when (preferences[THEME_KEY]) {
                "LIGHT" -> ThemeMode.LIGHT
                "DARK" -> ThemeMode.DARK
                "SYSTEM" -> ThemeMode.SYSTEM
                else -> ThemeMode.SYSTEM // Default to system
            }
        }

    val webSearchEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[WEB_SEARCH_KEY] ?: false // Default to disabled
        }

    val appLanguage: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] // null means system default
        }

    val embeddingEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[EMBEDDING_ENABLED_KEY] ?: false
        }

    val memoryEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[MEMORY_ENABLED_KEY] ?: false
        }

    val selectedEmbeddingModel: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_EMBEDDING_MODEL_KEY]
        }

    val autoReadoutEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[AUTO_READOUT_ENABLED_KEY] ?: false // Default to disabled
        }

    val FeatureFlags.PREMIUM_ENABLED: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_PREMIUM_KEY] ?: true
        }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeMode.name
        }
    }
    
    suspend fun setWebSearchEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[WEB_SEARCH_KEY] = enabled
        }
    }
    
    suspend fun setAppLanguage(languageCode: String?) {
        context.dataStore.edit { preferences ->
            if (languageCode != null) {
                preferences[LANGUAGE_KEY] = languageCode
            } else {
                preferences.remove(LANGUAGE_KEY)
            }
        }
    }

    suspend fun setEmbeddingEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[EMBEDDING_ENABLED_KEY] = enabled
        }
    }

    suspend fun setMemoryEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MEMORY_ENABLED_KEY] = enabled
        }
    }

    suspend fun setSelectedEmbeddingModel(modelName: String?) {
        context.dataStore.edit { preferences ->
            if (modelName != null) {
                preferences[SELECTED_EMBEDDING_MODEL_KEY] = modelName
            } else {
                preferences.remove(SELECTED_EMBEDDING_MODEL_KEY)
            }
        }
    }

    suspend fun setAutoReadoutEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_READOUT_ENABLED_KEY] = enabled
        }
    }

    suspend fun setIsPremium(premium: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_PREMIUM_KEY] = premium
        }
    }

    val githubStars: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[GITHUB_STARS_KEY] ?: 0 // Default to 0
        }

    suspend fun setGithubStars(stars: Int) {
        context.dataStore.edit { preferences ->
            preferences[GITHUB_STARS_KEY] = stars
        }
    }

    val anywaaModeEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ANYWAA_MODE_KEY] ?: false
        }

    suspend fun setAnywaaModeEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ANYWAA_MODE_KEY] = enabled
        }
    }

    val lowDataModeEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[LOW_DATA_MODE_KEY] ?: false
        }

    suspend fun setLowDataModeEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOW_DATA_MODE_KEY] = enabled
        }
    }

    val systemPrompt: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SYSTEM_PROMPT_KEY]
        }

    suspend fun setSystemPrompt(prompt: String?) {
        context.dataStore.edit { preferences ->
            if (prompt != null) {
                preferences[SYSTEM_PROMPT_KEY] = prompt
            } else {
                preferences.remove(SYSTEM_PROMPT_KEY)
            }
        }
    }
}
