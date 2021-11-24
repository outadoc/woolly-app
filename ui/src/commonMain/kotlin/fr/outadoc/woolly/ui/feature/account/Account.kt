package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import fr.outadoc.woolly.ui.richtext.RichText

@Composable
fun Account(
    modifier: Modifier = Modifier,
    account: Account,
    onAvatarClick: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = account,
            onClick = onAvatarClick
        )

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RichText(
                    modifier = Modifier
                        .alignByBaseline()
                        .fillMaxWidth(0.8f),
                    text = account.displayNameOrAcct,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    emojis = account.emojis
                )
            }

            if (account.displayName.isNotBlank()) {
                Text(
                    text = "@${account.acct}",
                    style = MaterialTheme.typography.titleSmall,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
