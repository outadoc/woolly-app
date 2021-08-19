package fr.outadoc.woolly.common.feature.favourites.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.favourites.viewmodel.FavouritesViewModel
import fr.outadoc.woolly.common.feature.status.ui.StatusList
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun FavouritesScreen(
    insets: PaddingValues,
    listState: LazyListState,
    onStatusClick: (Status) -> Unit = {}
) {
    val di = LocalDI.current
    val vm by di.instance<FavouritesViewModel>()

    StatusList(
        insets = insets,
        statusFlow = vm.favouritesPagingItems,
        lazyListState = listState,
        onStatusAction = vm::onStatusAction,
        onStatusClick = onStatusClick
    )
}
