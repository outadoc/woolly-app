package fr.outadoc.mastodonk.common

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
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.common.ui.StatusCard
import fr.outadoc.mastodonk.common.feature.publictimeline.ListState

@Composable
fun Timeline(modifier: Modifier = Modifier, state: ListState) {
    when (state) {
        ListState.Loading ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }

        is ListState.Content ->
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.page.contents) { item ->
                    StatusCard(item)
                }
            }
    }
}
