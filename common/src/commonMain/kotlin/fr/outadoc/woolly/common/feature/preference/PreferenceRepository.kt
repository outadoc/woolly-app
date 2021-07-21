package fr.outadoc.woolly.common.feature.preference

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    val preferences: Flow<AppPreferences>
    suspend fun updatePreferences(transform: (AppPreferences) -> AppPreferences)
}
