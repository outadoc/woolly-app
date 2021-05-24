package fr.outadoc.woolly.common.feature.tags.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.common.feature.status.ui.ErrorScreen
import fr.outadoc.woolly.common.ui.ListExtremityState
import kotlinx.coroutines.flow.Flow

@Composable
fun TagList(
    modifier: Modifier = Modifier,
    insets: PaddingValues,
    tagFlow: Flow<PagingData<Tag>>,
    lazyListState: LazyListState
) {
    val uriHandler = LocalUriHandler.current
    val lazyPagingItems = tagFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = insets
    ) {
        when (val state = lazyPagingItems.loadState.refresh) {
            LoadState.Loading -> item {
                Column(
                    modifier = Modifier.fillParentMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> item {
                ErrorScreen(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(16.dp),
                    error = state.error,
                    onRetry = lazyPagingItems::retry
                )
            }
        }

        item {
            ListExtremityState(
                state = lazyPagingItems.loadState.prepend,
                onRetry = lazyPagingItems::retry
            )
        }

        itemsIndexed(lazyPagingItems) { _, tag ->
            Column {
                if (tag != null) {
                    key(tag.name) {
                        HashtagListItem(
                            tag = tag,
                            onClick = {
                                uriHandler.openUri(tag.url)
                            }
                        )
                    }
                } else {
                    HashtagPlaceholder()
                }
            }
        }

        item {
            ListExtremityState(
                state = lazyPagingItems.loadState.append,
                onRetry = lazyPagingItems::retry
            )
        }
    }
}