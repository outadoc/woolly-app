package fr.outadoc.woolly.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
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
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import androidx.paging.map
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.woolly.common.feature.timeline.usecase.AnnotateStatusUseCase
import fr.outadoc.woolly.common.plus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    insets: PaddingValues,
    pagingSourceFactory: () -> PagingSource<PageInfo, Status>
) {
    val di = LocalDI.current
    val annotateStatusUseCase by di.instance<AnnotateStatusUseCase>()

    // Periodically refresh timestamps
    var currentTime by remember { mutableStateOf(Clock.System.now()) }
    rememberCoroutineScope().launch(Dispatchers.Default) {
        while (isActive) {
            delay(1_000)
            currentTime = Clock.System.now()
        }
    }

    val pager: Pager<PageInfo, Status> = remember {
        Pager(
            PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 200
            ),
            pagingSourceFactory = pagingSourceFactory
        )
    }

    val lazyPagingItems = pager.flow.map { pagingData ->
        withContext(Dispatchers.Default) {
            pagingData.map { status ->
                annotateStatusUseCase(status)
            }
        }
    }.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier,
        contentPadding = insets + 16.dp,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (val state = lazyPagingItems.loadState.refresh) {
            LoadState.Loading -> {
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

            is LoadState.Error -> {
                item {
                    CenteredErrorMessage(
                        error = state.error,
                        onRetry = lazyPagingItems::retry
                    )
                }
            }
        }

        itemsIndexed(lazyPagingItems) { _, item ->
            if (item != null) {
                StatusCard(
                    status = item,
                    currentTime = currentTime
                )
            } else {
                StatusPlaceholder()
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