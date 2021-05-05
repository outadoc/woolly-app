package fr.outadoc.mastodonk.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Status

@Composable
fun TimelineScreen(viewModel: TimelineViewModel) {
    val currentState = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Timeline")
                }
            )
        },
        content = {
            Timeline(state = currentState.value)
        }
    )
}

@Composable
fun Timeline(modifier: Modifier = Modifier, state: ListState) {
    when (state) {
        ListState.Loading ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(progress = 0.5f)
            }

        is ListState.Content ->
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
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
    Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "@${status.account.username}",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = status.content,
                style = MaterialTheme.typography.body2
            )
        }
    }
}
