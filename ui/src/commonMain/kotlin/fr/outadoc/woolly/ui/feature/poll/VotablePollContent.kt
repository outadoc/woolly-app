package fr.outadoc.woolly.ui.feature.poll

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Poll
import fr.outadoc.woolly.ui.richtext.RichText

@Composable
fun VotablePollContent(
    modifier: Modifier = Modifier,
    poll: Poll
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        poll.options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (poll.allowsMultipleChoices) {
                    Checkbox(
                        modifier = Modifier.size(16.dp),
                        checked = false,
                        enabled = false,
                        onCheckedChange = {}
                    )
                } else {
                    RadioButton(
                        modifier = Modifier.size(16.dp),
                        selected = false,
                        enabled = false,
                        onClick = {}
                    )
                }

                RichText(
                    modifier = Modifier.padding(start = 16.dp),
                    text = option.title,
                    emojis = poll.emojis,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
