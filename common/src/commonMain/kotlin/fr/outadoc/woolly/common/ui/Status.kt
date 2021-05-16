package fr.outadoc.woolly.common.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.timeline.AnnotatedStatus
import fr.outadoc.woolly.htmltext.NodeText
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Instant

@Composable
fun StatusCard(status: AnnotatedStatus, currentTime: Instant) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Status(
            status = status,
            currentTime = currentTime
        )
    }
}

@Composable
fun StatusPlaceholder() {
    Spacer(modifier = Modifier.height(64.dp))
}

@Composable
fun Status(status: AnnotatedStatus, currentTime: Instant) {
    Row(modifier = Modifier.padding(16.dp)) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = status.original.account
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            StatusHeader(
                modifier = Modifier.padding(bottom = 6.dp),
                status = status.original,
                currentTime = currentTime
            )
            SelectionContainer {
                NodeText(
                    textNodes = status.contentNodes,
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
            .size(48.dp)
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
fun StatusHeader(
    modifier: Modifier = Modifier,
    status: Status,
    currentTime: Instant
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.alignByBaseline().fillMaxWidth(0.8f),
                text = if (status.account.displayName.isNotBlank()) {
                    status.account.displayName
                } else {
                    "@${status.account.acct}"
                },
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            RelativeTime(
                modifier = Modifier.alignByBaseline(),
                currentTime = currentTime,
                time = status.createdAt,
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1
            )
        }

        if (status.account.displayName.isNotBlank()) {
            Text(
                text = "@${status.account.acct}",
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
