package fr.outadoc.woolly.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.plus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    insets: PaddingValues,
    statusFlow: Flow<PagingData<Status>>,
    lazyListState: LazyListState
) {
    // Periodically refresh timestamps
    var currentTime by remember { mutableStateOf(Clock.System.now()) }
    rememberCoroutineScope().launch(Dispatchers.Default) {
        while (isActive) {
            delay(1_000)
            currentTime = Clock.System.now()
        }
    }

    val lazyPagingItems = statusFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = insets + PaddingValues(top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
                CenteredErrorMessage(
                    error = state.error,
                    onRetry = lazyPagingItems::retry
                )
            }
        }

        itemsIndexed(lazyPagingItems) { _, item ->
            Column {
                if (item != null) {
                    StatusOrBoost(
                        status = item,
                        currentTime = currentTime
                    )
                } else {
                    StatusPlaceholder()
                }

                Divider(
                    thickness = 1.dp
                )
            }
        }

        if (lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                Column(
                    modifier = Modifier.fillParentMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
