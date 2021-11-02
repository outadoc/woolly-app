package fr.outadoc.woolly.ui.common

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

val LazyPagingItems<*>.errorStateForNotification: LoadState.Error?
    get() = (
            (loadState.refresh as? LoadState.Error)
                ?: (loadState.prepend as? LoadState.Error)
                ?: (loadState.append as? LoadState.Error)
            ).takeIf { itemCount > 0 }

val LazyPagingItems<*>.errorStateForDisplay: LoadState.Error?
    get() = (loadState.refresh as? LoadState.Error?)
        .takeIf { itemCount == 0 }
