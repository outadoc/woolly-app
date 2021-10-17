package fr.outadoc.woolly.ui.feature.poll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    RadioButton(
                        selected = false,
                        enabled = false,
                        onClick = {}
                    )
                } else {
                    Checkbox(
                        checked = false,
                        enabled = false,
                        onCheckedChange = {}
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
