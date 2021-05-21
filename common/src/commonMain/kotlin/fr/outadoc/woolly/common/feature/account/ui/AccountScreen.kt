package fr.outadoc.woolly.common.feature.account.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.ui.ProfilePicture
import fr.outadoc.woolly.htmltext.HtmlText
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource
import kotlinx.coroutines.Dispatchers
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun AccountScreen(insets: PaddingValues) {
    val di = LocalDI.current
    val repo by di.instance<AccountRepository>()

    val currentAccount by repo.currentAccount.collectAsState()

    Box(modifier = Modifier.padding(insets)) {
        currentAccount?.let { AccountHeader(it) }
    }
}

@Composable
fun AccountHeader(account: Account) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val headerResource = lazyImageResource(account.headerStaticUrl) {
            dispatcher = Dispatchers.IO
        }

        KamelImage(
            modifier = Modifier.aspectRatio(4f),
            resource = headerResource,
            contentDescription = "Your profile header",
            crossfade = true,
            animationSpec = tween(),
            contentScale = ContentScale.FillWidth
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .offset(y = -(80.dp))
        ) {
            ProfilePicture(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(96.dp),
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
                    color = LocalContentColor.current.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            SelectionContainer {
                HtmlText(
                    html = account.bio,
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.body1
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AccountStat(
                    modifier = Modifier.alignByBaseline(),
                    number = account.statusesCount.toString(),
                    unit = "Toots"
                )

                AccountStat(
                    modifier = Modifier.alignByBaseline(),
                    number = account.followingCount.toString(),
                    unit = "Following"
                )

                AccountStat(
                    modifier = Modifier.alignByBaseline(),
                    number = account.followersCount.toString(),
                    unit = "Followers"
                )
            }
        }
    }
}

@Composable
fun AccountStat(modifier: Modifier = Modifier, number: String, unit: String) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                append(number)
            }

            append(" ")
            append(unit)
        }
    )
}
