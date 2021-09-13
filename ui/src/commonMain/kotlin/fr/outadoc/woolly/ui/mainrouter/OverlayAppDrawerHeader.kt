package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun OverlayAppDrawerHeader(
    modifier: Modifier = Modifier,
    account: Account,
    onAvatarClick: () -> Unit = {}
) {
    Box(modifier = modifier) {
        KamelImage(
            modifier = Modifier.fillMaxSize(),
            resource = lazyPainterResource(account.headerStaticUrl),
            contentDescription = "Your profile header",
            crossfade = true,
            contentScale = ContentScale.Crop
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black.copy(alpha = 0.5f),
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfilePicture(
                    modifier = Modifier.padding(end = 16.dp),
                    account = account,
                    onClick = onAvatarClick
                )

                Column {
                    Text(
                        text = account.displayNameOrAcct,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (account.displayName.isNotBlank()) {
                        Text(
                            text = "@${account.acct}",
                            style = MaterialTheme.typography.subtitle2,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
