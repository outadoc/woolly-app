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
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.htmltext.HtmlParser
import fr.outadoc.woolly.htmltext.NodeText
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Instant
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun StatusCard(status: Status, currentTime: Instant) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        StatusOrBoost(
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
fun StatusOrBoost(status: Status, currentTime: Instant) {
    val original = status.boostedStatus ?: status
    val boostedBy = if (status.boostedStatus != null) status.account else null
    Status(original, boostedBy, currentTime)
}

@Composable
fun Status(status: Status, boostedBy: Account?, currentTime: Instant) {
    val di = LocalDI.current
    val parser by di.instance<HtmlParser>()

    val formattedText = remember(status.content) {
        parser.parse(status.content)
    }

    Row(modifier = Modifier.padding(16.dp)) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = status.account
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            StatusHeader(
                modifier = Modifier.padding(bottom = 8.dp),
                status = status,
                currentTime = currentTime
            )

            SelectionContainer {
                NodeText(
                    textNodes = formattedText,
                    style = MaterialTheme.typography.body2
                )
            }

            StatusFooter(
                modifier = Modifier.padding(top = 8.dp),
                boostedBy = boostedBy
            )
        }
    }
}

@Composable
fun StatusFooter(modifier: Modifier = Modifier, boostedBy: Account?) {
    Column(modifier = modifier) {
        boostedBy?.let { boostedBy ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp),
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Boosted",
                    tint = LocalContentColor.current.copy(alpha = 0.7f)
                )

                Text(
                    "${boostedBy.displayNameOrAcct} boosted",
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    color = LocalContentColor.current.copy(alpha = 0.7f),
                    overflow = TextOverflow.Ellipsis
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
            contentDescription = "${account.displayNameOrAcct}'s profile picture",
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
                text = status.account.displayNameOrAcct,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            RelativeTime(
                modifier = Modifier.alignByBaseline(),
                currentTime = currentTime,
                time = status.createdAt,
                style = MaterialTheme.typography.subtitle2,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                maxLines = 1
            )
        }

        if (status.account.displayName.isNotBlank()) {
            Text(
                text = "@${status.account.acct}",
                style = MaterialTheme.typography.subtitle2,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

val Account.displayNameOrAcct: String
    get() = if (displayName.isNotBlank()) displayName else "@$acct"
