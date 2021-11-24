package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.ui.common.FillFirstThenWrap
import fr.outadoc.woolly.ui.richtext.RichText

@Composable
fun StatusHeader(
    modifier: Modifier = Modifier,
    status: Status
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            horizontalArrangement = FillFirstThenWrap,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RichText(
                modifier = Modifier
                    .alignByBaseline()
                    .weight(1f, fill = false)
                    .padding(end = 8.dp),
                text = status.account.displayNameOrAcct,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                emojis = status.account.emojis
            )

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                StatusVisibilityIcon(
                    modifier = Modifier.size(16.dp),
                    visibility = status.visibility
                )

                PastRelativeTime(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .alignByBaseline(),
                    time = status.createdAt,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            if (status.account.displayName.isNotBlank()) {
                Text(
                    text = "@${status.account.acct}",
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (status.account.isBot == true) {
            StatusAutomatedLabel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }
    }
}
