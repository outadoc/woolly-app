package fr.outadoc.woolly.common.feature.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferenceRepositoryImpl(
    private val prefs: DataStore<Preferences>,
    private val json: Json
) : PreferenceRepository {

    companion object {
        private val KEY_PREFERENCES = stringPreferencesKey("preferences")
    }

    override val preferences: Flow<AppPreferences> = prefs.data
        .map { it[KEY_PREFERENCES].decodePreferencesOrDefault() }
        .distinctUntilChanged()

    override suspend fun updatePreferences(transform: (AppPreferences) -> AppPreferences) {
        withContext(Dispatchers.IO) {
            prefs.edit { preferences ->
                val currentValue = preferences[KEY_PREFERENCES].decodePreferencesOrDefault()
                preferences[KEY_PREFERENCES] = json.encodeToString(transform(currentValue))
            }
        }
    }

    private fun String?.decodePreferencesOrDefault(): AppPreferences {
        return if (this == null) AppPreferences()
        else json.decodeFromString(this)
    }
}