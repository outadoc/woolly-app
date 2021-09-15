package fr.outadoc.woolly.common.feature.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import fr.outadoc.woolly.common.LoadState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferenceRepositoryImpl(
    appScope: CoroutineScope,
    private val prefs: DataStore<Preferences>,
    private val json: Json = Json.Default
) : PreferenceRepository {

    companion object {
        private val KEY_PREFERENCES = stringPreferencesKey("preferences")
    }

    override val preferences: StateFlow<LoadState<AppPreferences>> =
        prefs.data
            .map { preferences ->
                LoadState.Loaded(
                    preferences[KEY_PREFERENCES].decodePreferencesOrDefault()
                ) as LoadState<AppPreferences>
            }
            .onStart { emit(LoadState.Loading()) }
            .distinctUntilChanged()
            .stateIn(
                scope = appScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = LoadState.Loading()
            )

    override suspend fun updatePreferences(transform: (AppPreferences) -> AppPreferences) {
        withContext(Dispatchers.IO) {
            prefs.edit { preferences ->
                // Fix editing on Windows, where the destination file has to be deleted
                // before calling moveTo
                PreferenceFileProvider.preferenceFile?.delete()

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