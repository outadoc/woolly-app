package fr.outadoc.woolly.common.feature.navigation

import androidx.compose.foundation.lazy.LazyListState

suspend fun LazyListState.tryScrollToTop() = try {
    animateScrollToItem(0)
} catch (e: NoSuchElementException) {
    // List was empty
}
