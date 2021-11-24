package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Emoji
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.richtext.RichText
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun ContentWarningBanner(
    modifier: Modifier = Modifier,
    contentWarning: String,
    isCollapsed: Boolean,
    emojis: List<Emoji> = emptyList(),
    onToggle: () -> Unit
) {
    Card(modifier = modifier.clickable { onToggle() }) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f, fill = false),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = stringResource(MR.strings.contentWarning_cd),
                    modifier = Modifier.padding(end = 16.dp)
                )

                RichText(
                    modifier = Modifier.padding(end = 8.dp),
                    text = contentWarning,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    emojis = emojis
                )
            }

            if (isCollapsed) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(MR.strings.contentWarning_expand_cd)
                )
            } else {
                Icon(
                    Icons.Default.ArrowDropUp,
                    contentDescription = stringResource(MR.strings.contentWarning_collapse_cd)
                )
            }
        }
    }
}
