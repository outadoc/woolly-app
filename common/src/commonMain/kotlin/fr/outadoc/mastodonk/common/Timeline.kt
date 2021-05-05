package fr.outadoc.mastodonk.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Status

@Composable
fun PublicTimeline(viewModel: TimelineViewModel) {
    val currentState = viewModel.state.collectAsState()
    Timeline(currentState.value)
}

@Composable
fun Timeline(state: ListState) {
    when (state) {
        ListState.Loading -> Text(text = "Loading")
        is ListState.Content ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.page.contents) { item ->
                    Status(item)
                }
            }
    }
}

@Composable
fun Status(status: Status) {
    Column {
        Text(
            text = "@${status.account.username}",
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = status.content,
            style = MaterialTheme.typography.body1
        )
    }
}
