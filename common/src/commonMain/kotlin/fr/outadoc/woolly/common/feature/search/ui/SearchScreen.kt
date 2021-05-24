package fr.outadoc.woolly.common.feature.search.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.woolly.common.feature.account.ui.AccountList
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.search.viewmodel.SearchViewModel
import fr.outadoc.woolly.common.feature.status.ui.StatusList
import fr.outadoc.woolly.common.feature.tags.ui.TagList
import fr.outadoc.woolly.common.feature.tags.ui.TrendingScreen
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun SearchScreen(
    insets: PaddingValues,
    currentSubScreen: SearchSubScreen,
    statusListState: LazyListState,
    accountsListState: LazyListState,
    hashtagsListState: LazyListState
) {
    val di = LocalDI.current
    val vm by di.instance<SearchViewModel>()
    val state by vm.state.collectAsState()

    if (state.query.isEmpty()) {
        TrendingScreen(insets = insets)
    } else {
        when (currentSubScreen) {
            SearchSubScreen.Statuses -> StatusList(
                insets = insets,
                statusFlow = vm.statusPagingItems,
                lazyListState = statusListState,
                onStatusAction = vm::onStatusAction
            )

            SearchSubScreen.Accounts -> AccountList(
                insets = insets,
                accountFlow = vm.accountsPagingItems,
                lazyListState = accountsListState
            )

            SearchSubScreen.Hashtags -> TagList(
                insets = insets,
                tagFlow = vm.hashtagsPagingItems,
                lazyListState = hashtagsListState
            )
        }
    }
}
