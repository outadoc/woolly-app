package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.status.formatShort
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.common.BulletSeparator
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import fr.outadoc.woolly.ui.feature.status.StatusAutomatedLabel
import fr.outadoc.woolly.ui.richtext.RichText
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun AccountInfo(
    modifier: Modifier = Modifier,
    account: Account,
    isFollowing: Boolean? = null,
    onFollowClick: (Boolean) -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfilePicture(
                modifier = Modifier.padding(bottom = 16.dp),
                size = 96.dp,
                account = account,
                onClick = { uriHandler.openUri(account.avatarUrl) }
            )

            when (isFollowing) {
                true -> Button(onClick = { onFollowClick(false) }) {
                    Text(text = stringResource(MR.strings.accountDetails_unfollow_action))
                }
                false -> OutlinedButton(onClick = { onFollowClick(true) }) {
                    Text(text = stringResource(MR.strings.accountDetails_follow_action))
                }
                null -> {
                }
            }
        }

        RichText(
            text = account.displayNameOrAcct,
            style = MaterialTheme.typography.h6,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            emojis = account.emojis
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AccountStat(
                modifier = Modifier.alignByBaseline(),
                number = account.statusesCount.formatShort(forceEnglishLocale = false),
                unit = stringResource(MR.strings.accountDetails_postCount_title)
            )

            BulletSeparator()

            AccountStat(
                modifier = Modifier.alignByBaseline(),
                number = account.followingCount.formatShort(forceEnglishLocale = false),
                unit = stringResource(MR.strings.accountDetails_followingCount_title)
            )

            BulletSeparator()

            AccountStat(
                modifier = Modifier.alignByBaseline(),
                number = account.followersCount.formatShort(forceEnglishLocale = false),
                unit = stringResource(MR.strings.accountDetails_followerCount_title)
            )
        }
    }
}
