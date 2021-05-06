package fr.outadoc.mastodonk.common

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import fr.outadoc.compose.htmltext.HtmlText
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource
import kotlinx.coroutines.Dispatchers

@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel,
    toggleDarkMode: () -> Unit
) {
    val currentState = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Public Timeline")
                },
                actions = {
                    IconButton(onClick = {
                        toggleDarkMode()
                    }) {
                        Icon(Icons.Default.LightMode, "Toggle dark theme")
                    }
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

@Composable
fun StatusCard(status: Status) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Status(status)
    }
}

@Composable
fun Status(status: Status) {
    Row(modifier = Modifier.padding(16.dp)) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = status.account
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            StatusHeader(
                modifier = Modifier.padding(bottom = 6.dp),
                status = status
            )
            SelectionContainer {
                HtmlText(
                    text = status.content,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun ProfilePicture(modifier: Modifier = Modifier, account: Account) {
    val avatarResource = lazyImageResource(account.avatarStaticUrl) {
        dispatcher = Dispatchers.IO
    }

    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
    ) {
        KamelImage(
            resource = avatarResource,
            contentDescription = "${account.displayName}'s profile picture",
            crossfade = true,
            animationSpec = tween()
        )
    }
}

@Composable
fun StatusHeader(modifier: Modifier = Modifier, status: Status) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier.alignByBaseline(),
            text = "@${status.account.username}",
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            modifier = Modifier
                .alignByBaseline()
                .padding(start = 8.dp),
            text = status.account.displayName,
            style = MaterialTheme.typography.subtitle2
        )
    }
}
