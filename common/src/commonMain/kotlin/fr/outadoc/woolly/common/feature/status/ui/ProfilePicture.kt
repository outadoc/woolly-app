package fr.outadoc.woolly.common.feature.status.ui

import androidx.compose.animation.core.tween
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
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.ui.WoollyDefaults
import fr.outadoc.woolly.common.ui.WoollyTheme
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    account: Account,
    onClick: (() -> Unit)? = null
) {
    val avatarResource = lazyImageResource(account.avatarStaticUrl) {
        dispatcher = Dispatchers.IO
    }

    val contentDescription = "${account.displayNameOrAcct}'s profile picture"

    Surface(
        modifier = modifier.size(WoollyDefaults.AvatarSize),
        enabled = onClick != null,
        onClick = { onClick?.invoke() },
        shape = WoollyTheme.AvatarShape,
        elevation = 4.dp
    ) {
        KamelImage(
            modifier = Modifier.size(WoollyDefaults.AvatarSize),
            resource = avatarResource,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillWidth,
            crossfade = true,
            onLoading = {
                Icon(
                    modifier = Modifier
                        .alpha(0.5f)
                        .size(WoollyDefaults.AvatarSize),
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
