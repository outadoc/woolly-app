package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.ui.common.WoollyDefaults
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
