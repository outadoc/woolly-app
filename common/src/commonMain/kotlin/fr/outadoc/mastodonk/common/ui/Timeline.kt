package fr.outadoc.mastodonk.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.common.PageState
import fr.outadoc.mastodonk.common.plus

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    insets: PaddingValues,
    state: PageState<List<Status>>
) {
    when (state) {
        is PageState.Loading ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }

        is PageState.Content ->
            LazyColumn(
                modifier = modifier,
                contentPadding = insets + 16.dp,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.page.contents) { item ->
                    StatusCard(item)
                }
            }
    }
}
