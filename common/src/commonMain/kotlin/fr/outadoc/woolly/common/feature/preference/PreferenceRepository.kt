package fr.outadoc.woolly.common.feature.preference

import fr.outadoc.woolly.common.LoadState
import kotlinx.coroutines.flow.StateFlow

interface PreferenceRepository {

    val preferences: StateFlow<LoadState<AppPreferences>>
    suspend fun updatePreferences(transform: (AppPreferences) -> AppPreferences)
}
