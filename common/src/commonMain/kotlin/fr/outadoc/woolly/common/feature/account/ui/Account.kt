package fr.outadoc.woolly.common.feature.account.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.status.ui.ProfilePicture
import fr.outadoc.woolly.common.richtext.RichText

@Composable
fun AccountPlaceholder() {
    Spacer(modifier = Modifier.height(128.dp))
}

@Composable
fun Account(
    modifier: Modifier = Modifier,
    account: Account
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = account
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
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    emojis = account.emojis
                )
            }

            if (account.displayName.isNotBlank()) {
                Text(
                    text = "@${account.acct}",
                    style = MaterialTheme.typography.subtitle2,
                    color = LocalContentColor.current.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
