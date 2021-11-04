package fr.outadoc.woolly.ui.common

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

private const val PlaceholderCount = 15

fun LazyListScope.itemPlaceholders(
    items: LazyPagingItems<*>,
    content: @Composable () -> Unit
) {
    if (items.itemCount == 0 && items.loadState.refresh is LoadState.Loading) {
        items(arrayOfNulls<Any>(PlaceholderCount)) {
            content()
        }
    }
}
