package fr.outadoc.woolly.ui.feature.poll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Poll
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.common.BulletSeparator
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun PollFooter(
    modifier: Modifier = Modifier,
    poll: Poll,
    onClickVote: (() -> Unit)?
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (onClickVote != null) {
            Button(onClick = onClickVote) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(MR.strings.poll_vote_action))
                    Icon(
                        modifier = Modifier.padding(start = 4.dp),
                        imageVector = Icons.Default.OpenInBrowser,
                        contentDescription = stringResource(MR.strings.poll_openInBrowser_cd)
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                modifier = Modifier.alignByBaseline(),
                text = stringResource(MR.strings.poll_votes_label, poll.votesCount)
            )

            BulletSeparator(
                modifier = Modifier.alignByBaseline()
            )

            val expiresAt = poll.expiresAt

            when {
                poll.isExpired -> {
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = stringResource(MR.strings.poll_finalResults_label)
                    )
                }
                expiresAt != null -> {
                    FutureRelativeTime(
                        modifier = Modifier.alignByBaseline(),
                        time = expiresAt,
                        expiredLabel = stringResource(MR.strings.poll_expirationTime_expired)
                    )
                }
            }
        }
    }
}
