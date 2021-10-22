package fr.outadoc.woolly.common.feature.state

import androidx.compose.foundation.lazy.LazyListState
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.consume

@Parcelize
private data class ListStateParcel(
    val firstVisibleItemIndex: Int,
    val firstVisibleItemScrollOffset: Int
) : Parcelable

private fun LazyListState.parcelize() = ListStateParcel(
    firstVisibleItemIndex = firstVisibleItemIndex,
    firstVisibleItemScrollOffset = firstVisibleItemScrollOffset
)

private fun ListStateParcel.unparcelize() = LazyListState(
    firstVisibleItemIndex = firstVisibleItemIndex,
    firstVisibleItemScrollOffset = firstVisibleItemScrollOffset
)

fun StateKeeper.consumeListStateOrDefault(
    key: String = "list_state",
    default: LazyListState = LazyListState()
): LazyListState {
    return consume<ListStateParcel>(key)?.unparcelize()
        ?: default
}

fun StateKeeper.registerListState(
    key: String = "list_state",
    block: () -> LazyListState
) {
    register(key) { block().parcelize() }
}
