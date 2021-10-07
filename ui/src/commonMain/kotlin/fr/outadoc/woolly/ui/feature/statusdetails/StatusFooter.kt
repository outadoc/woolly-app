package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.ui.common.BulletSeparator
import fr.outadoc.woolly.ui.feature.status.StatusVisibilityIcon

@Composable
fun StatusFooter(
    modifier: Modifier = Modifier,
    status: Status
) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatusVisibilityIcon(
            modifier = Modifier.size(16.dp),
            visibility = status.visibility
        )

        AbsoluteTime(
            modifier = Modifier
                .alignByBaseline()
                .clickable { status.url?.let { uriHandler.openUri(it) } },
            time = status.createdAt,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle2
        )

        status.application?.let { app ->
            BulletSeparator()

            Text(
                modifier = Modifier
                    .alignByBaseline()
                    .clickable { app.website?.let { uriHandler.openUri(it) } },
                text = app.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}
