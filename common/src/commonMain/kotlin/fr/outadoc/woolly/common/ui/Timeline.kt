package fr.outadoc.woolly.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    insets: PaddingValues,
    source: PagingSource<PageInfo, Status>
) {
    val di = LocalDI.current
    val annotateStatusUseCase by di.instance<AnnotateStatusUseCase>()

    val pager: Pager<PageInfo, Status> = remember {
        Pager(
            PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) { source }
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

        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        itemsIndexed(lazyPagingItems) { index, item ->
            if (item != null) {
                StatusCard(item)
            }
        }

        if (lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
