package fr.outadoc.woolly.common.feature.preference

import fr.outadoc.woolly.common.LoadState
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    val preferences: Flow<LoadState<AppPreferences>>
    suspend fun updatePreferences(transform: (AppPreferences) -> AppPreferences)
}
