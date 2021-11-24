package fr.outadoc.woolly.ui.feature.poll

import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Poll
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.richtext.RichText
import fr.outadoc.woolly.ui.strings.stringResource
import kotlin.math.roundToInt

@Composable
fun PollResultsContent(
    modifier: Modifier = Modifier,
    poll: Poll
) {
    val ownVotes = poll.ownVotes ?: emptyList()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        poll.options.forEachIndexed { index, option ->
            val totalVotes = poll.votesCount.toFloat()
            val optionVotes = option.votesCount?.toFloat() ?: 0f
            val ratio = if (totalVotes == 0f) 0f else (optionVotes / totalVotes)

            Box(modifier = Modifier.height(32.dp)) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium),
                    progress = ratio
                )

                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.width(64.dp),
                        text = "${(ratio * 100).roundToInt()} %",
                        fontWeight = FontWeight.Bold
                    )

                    if (index in ownVotes) {
                        Icon(
                            modifier = Modifier
                                .size(28.dp)
                                .padding(end = 8.dp),
                            imageVector = Icons.Default.TaskAlt,
                            contentDescription = stringResource(MR.strings.poll_chosenOption_cd)
                        )
                    }

                    RichText(
                        text = option.title,
                        emojis = poll.emojis,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
