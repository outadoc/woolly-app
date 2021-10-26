package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun StatusBoostedByLabel(
    modifier: Modifier = Modifier,
    boostedBy: Account,
    onAccountClick: (Account) -> Unit = {}
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp),
                imageVector = Icons.Default.Repeat,
                contentDescription = stringResource(MR.strings.status_boosted_cd),
                tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )

            ProfilePicture(
                modifier = Modifier.padding(end = 4.dp),
                account = boostedBy,
                size = 20.dp,
                onClick = { onAccountClick(boostedBy) }
            )

            Text(
                modifier = Modifier.clickable { onAccountClick(boostedBy) },
                text = stringResource(
                    MR.strings.status_boosted_message,
                    boostedBy.displayNameOrAcct
                ),
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
