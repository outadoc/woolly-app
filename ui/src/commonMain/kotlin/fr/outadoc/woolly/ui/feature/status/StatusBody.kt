package fr.outadoc.woolly.ui.feature.status

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Status

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StatusBody(
    modifier: Modifier = Modifier,
    status: Status
) {
    if (status.contentWarningText.isNotBlank()) {
        var isCollapsed by remember { mutableStateOf(true) }

        ContentWarningBanner(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            contentWarning = status.contentWarningText,
            isCollapsed = isCollapsed,
            onToggle = { isCollapsed = !isCollapsed },
            emojis = status.emojis
        )

        AnimatedVisibility(
            visible = !isCollapsed,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
        ) {
            StatusBodyPlain(
                modifier = modifier.padding(top = 8.dp),
                status = status
            )
        }

    } else {
        StatusBodyPlain(
            modifier = modifier,
            status = status
        )
    }
}
