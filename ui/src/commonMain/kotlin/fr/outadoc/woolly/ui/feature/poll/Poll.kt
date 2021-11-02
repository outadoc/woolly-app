package fr.outadoc.woolly.ui.feature.poll

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Poll

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Poll(
    modifier: Modifier = Modifier,
    poll: Poll,
    onClick: () -> Unit
) {
    val isVotable = !poll.isExpired && poll.hasVoted != true

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = isVotable
    ) {
        Column {
            if (isVotable) {
                VotablePollContent(
                    modifier = Modifier.padding(8.dp),
                    poll = poll
                )
            } else {
                PollResultsContent(
                    modifier = Modifier.padding(16.dp),
                    poll = poll
                )
            }

            Divider(modifier = Modifier.height(1.dp))

            PollFooter(
                modifier = Modifier.padding(16.dp),
                poll = poll,
                onClickVote = if (isVotable) onClick else null
            )
        }
    }
}
