package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.status.formatShort
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import fr.outadoc.woolly.ui.feature.status.StatusAutomatedLabel
import fr.outadoc.woolly.ui.richtext.RichText

@Composable
fun AccountInfo(account: Account) {
    ProfilePicture(
        modifier = Modifier.padding(bottom = 16.dp),
        size = 96.dp,
        account = account
    )

    Text(
        text = account.displayNameOrAcct,
        style = MaterialTheme.typography.h6,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

    if (account.displayName.isNotBlank()) {
        Text(
            text = "@${account.acct}",
            style = MaterialTheme.typography.subtitle1,
            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    if (account.isBot == true) {
        StatusAutomatedLabel(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
    }

    if (!account.fields.isNullOrEmpty()) {
        AccountFields(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            fields = account.fields!!,
            emojis = account.emojis
        )
    }

    SelectionContainer {
        RichText(
            modifier = Modifier.padding(top = 16.dp),
            text = account.bio,
            style = MaterialTheme.typography.body1,
            emojis = account.emojis
        )
    }

    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        AccountStat(
            modifier = Modifier.alignByBaseline(),
            number = account.statusesCount.formatShort(),
            unit = "Posts"
        )

        AccountStat(
            modifier = Modifier.alignByBaseline(),
            number = account.followingCount.formatShort(),
            unit = "Following"
        )

        AccountStat(
            modifier = Modifier.alignByBaseline(),
            number = account.followersCount.formatShort(),
            unit = "Followers"
        )
    }
}
