package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Field
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import fr.outadoc.woolly.ui.richtext.RichText
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource


@Composable
fun AccountHeader(
    modifier: Modifier = Modifier,
    account: Account,
    maxContentWidth: Dp = WoollyDefaults.MaxContentWidth
) {
    Column(modifier = modifier.fillMaxWidth()) {
        KamelImage(
            modifier = Modifier.aspectRatio(4f),
            resource = lazyPainterResource(account.headerStaticUrl),
            contentDescription = "Your profile header",
            crossfade = true,
            contentScale = ContentScale.FillWidth,
            onLoading = {
                Spacer(modifier = Modifier.aspectRatio(4f))
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(max = maxContentWidth)
                    .offset(y = -(80.dp))
            ) {
                AccountInfo(account = account)
            }
        }
    }
}

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
            color = LocalContentColor.current.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    if (!account.fields.isNullOrEmpty()) {
        AccountFields(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            fields = account.fields!!
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AccountStat(
            modifier = Modifier.alignByBaseline(),
            number = account.statusesCount.toString(),
            unit = "Posts"
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

@Composable
fun AccountFields(modifier: Modifier = Modifier, fields: List<Field>) {
    Column(modifier = modifier) {
        fields.forEach { field ->
            Row {
                Text(
                    text = field.name,
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                )

                SelectionContainer {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = field.value
                    )
                }
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
