package fr.outadoc.woolly.common.feature.status.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.ui.WoollyDefaults
import fr.outadoc.woolly.common.ui.WoollyTheme
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource
import kotlinx.coroutines.Dispatchers

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    account: Account
) {
    val avatarResource = lazyImageResource(account.avatarStaticUrl) {
        dispatcher = Dispatchers.IO
    }

    val contentDescription = "${account.displayNameOrAcct}'s profile picture"

    KamelImage(
        resource = avatarResource,
        contentDescription = contentDescription,
        contentScale = ContentScale.FillWidth,
        crossfade = true,
        modifier = modifier
            .size(WoollyDefaults.AvatarSize)
            .clip(WoollyTheme.AvatarShape),
        onLoading = {
            Icon(
                modifier = modifier.size(WoollyDefaults.AvatarSize),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = contentDescription
            )
        },
        animationSpec = tween()
    )
}
