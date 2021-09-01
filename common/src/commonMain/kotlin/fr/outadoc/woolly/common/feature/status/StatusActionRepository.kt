package fr.outadoc.woolly.common.feature.status

import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatusActionRepository(
    scope: CoroutineScope,
    clientProvider: MastodonClientProvider
) {
    private val _actionFlow = MutableSharedFlow<StatusAction>(replay = 1)
    private val listeners = mutableSetOf<(StatusAction) -> Unit>()

    init {
        scope.launch(Dispatchers.IO) {
            clientProvider.mastodonClient
                .filterNotNull()
                .combine(_actionFlow) { client, action ->
                    with(client.statuses) {
                        when (action) {
                            is StatusAction.Favourite -> favourite(action.status.statusId)
                            is StatusAction.UndoFavourite -> undoFavourite(action.status.statusId)
                            is StatusAction.Boost -> boost(action.status.statusId)
                            is StatusAction.UndoBoost -> undoBoost(action.status.statusId)
                            is StatusAction.Bookmark -> bookmark(action.status.statusId)
                            is StatusAction.UndoBookmark -> undoBookmark(action.status.statusId)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        listeners.forEach { listener -> listener(action) }
                    }

                }
                .collect()
        }
    }

    fun addOnActionPerformedListener(listener: (StatusAction) -> Unit) {
        listeners.add(listener)
    }

    fun onStatusAction(action: StatusAction) {
        _actionFlow.tryEmit(action)
    }
}