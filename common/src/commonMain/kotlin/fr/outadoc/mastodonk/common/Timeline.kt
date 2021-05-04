package fr.outadoc.mastodonk.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.client.MastodonClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Timeline(client: MastodonClient) {
    var currentState: ListState = remember { ListState.Loading }

    rememberCoroutineScope().launch(Dispatchers.IO) {
        currentState = ListState.Content(client.timelines.getPublicTimeline())
    }

    when (val state = currentState) {
        ListState.Loading -> Text(text = "Loading")
        is ListState.Content -> LazyColumn {
            items(state.page.contents) { item ->
                Status(item)
            }
        }
    }
}

@Composable
fun Status(status: Status) {
    Column {
        Text(text = status.account.username)
        Text(text = status.content)
    }
}
