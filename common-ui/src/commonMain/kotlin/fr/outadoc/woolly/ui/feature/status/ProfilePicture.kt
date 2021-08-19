package fr.outadoc.woolly.ui.feature.status

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.common.WoollyTheme
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    account: Account,
    size: Dp = WoollyDefaults.AvatarSize,
    onClick: (() -> Unit)? = null
) {
    val avatarResource = lazyImageResource(account.avatarStaticUrl) {
        dispatcher = Dispatchers.IO
    }

    val contentDescription = "${account.displayNameOrAcct}'s profile picture"

    Surface(
        modifier = modifier
            .size(size)
            .apply {
                if (onClick != null) {
                    clickable { onClick() }
                }
            },
        shape = WoollyTheme.AvatarShape,
        elevation = 4.dp
    ) {
        KamelImage(
            modifier = Modifier.size(size),
            resource = avatarResource,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillWidth,
            crossfade = true,
            onLoading = {
                Icon(
                    modifier = Modifier
                        .alpha(0.5f)
                        .size(size),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = contentDescription
                )
            },
            onFailure = {
                Icon(
                    modifier = Modifier
                        .alpha(0.5f)
                        .size(WoollyDefaults.AvatarSize),
                    imageVector = Icons.Default.NoAccounts,
                    contentDescription = contentDescription
                )
            },
            animationSpec = tween()
        )
    }
}
