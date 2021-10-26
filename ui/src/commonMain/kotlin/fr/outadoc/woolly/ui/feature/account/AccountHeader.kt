package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.strings.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun AccountHeader(
    modifier: Modifier = Modifier,
    account: Account,
    maxContentWidth: Dp = WoollyDefaults.MaxContentWidth,
    headerHeight: Dp = WoollyDefaults.HeaderHeight,
    isFollowing: Boolean? = null,
    onFollowClick: (Boolean) -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        KamelImage(
            modifier = Modifier
                .height(headerHeight)
                .clickable {
                    uriHandler.openUri(account.headerUrl)
                },
            resource = lazyPainterResource(account.headerStaticUrl),
            contentDescription = stringResource(MR.strings.accountDetails_header_cd),
            crossfade = true,
            contentScale = ContentScale.Crop,
            onLoading = {
                Spacer(modifier = Modifier.height(headerHeight))
            },
            onFailure = {
                Spacer(modifier = Modifier.height(headerHeight))
            }
        )

        Row(
            modifier = Modifier
                .padding(top = headerHeight - 72.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AccountInfo(
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(max = maxContentWidth),
                account = account,
                isFollowing = isFollowing,
                onFollowClick = onFollowClick
            )
        }
    }
}
