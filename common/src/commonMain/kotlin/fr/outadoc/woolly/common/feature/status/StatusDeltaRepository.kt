package fr.outadoc.woolly.common.feature.status

import fr.outadoc.mastodonk.api.endpoint.statuses.StatusesApi
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StatusDeltaRepository(
    scope: CoroutineScope,
    clientProvider: MastodonClientProvider
): StatusDeltaConsumer, StatusDeltaSupplier {

    private val _actionFlow = MutableSharedFlow<StatusAction>(replay = 1)

    private val _cachedStatusDeltas = MutableStateFlow<Map<String, StatusDelta>>(emptyMap())
    override val statusDeltas = _cachedStatusDeltas.asStateFlow()

    init {
        scope.launch {
            clientProvider.mastodonClient
                .filterNotNull()
                .combine(_actionFlow) { client, action ->
                    val originalDelta = statusDeltas.value[action.status.statusId]
                    val newDelta = (originalDelta ?: StatusDelta()).performAction(action)
                    updateDeltasWith(action.status.statusId, newDelta)

                    try {
                        client.statuses.performAction(action)
                    } catch (e: Exception) {
                        updateDeltasWith(action.status.statusId, originalDelta)
                    }
                }
                .collect()
        }
    }

    override fun onStatusAction(action: StatusAction) {
        _actionFlow.tryEmit(action)
    }

    private fun StatusDelta.performAction(action: StatusAction): StatusDelta {
        return when (action) {
            is StatusAction.Favourite -> copy(isFavourited = true)
            is StatusAction.UndoFavourite -> copy(isFavourited = false)
            is StatusAction.Boost -> copy(isBoosted = true)
            is StatusAction.UndoBoost -> copy(isBoosted = false)
            is StatusAction.Bookmark -> copy(isBookmarked = true)
            is StatusAction.UndoBookmark -> copy(isBookmarked = false)
        }
    }

    private suspend fun StatusesApi.performAction(action: StatusAction) {
        val statusId = action.status.statusId
        when (action) {
            is StatusAction.Favourite -> favourite(statusId)
            is StatusAction.UndoFavourite -> undoFavourite(statusId)
            is StatusAction.Boost -> boost(statusId)
            is StatusAction.UndoBoost -> undoBoost(statusId)
            is StatusAction.Bookmark -> bookmark(statusId)
            is StatusAction.UndoBookmark -> undoBookmark(statusId)
        }
    }

    private fun updateDeltasWith(statusId: String, statusDelta: StatusDelta?) {
        _cachedStatusDeltas.value =
            (if (statusDelta == null) _cachedStatusDeltas.value - statusId
            else _cachedStatusDeltas.value + (statusId to statusDelta))
    }
}