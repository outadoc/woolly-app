package fr.outadoc.woolly.common.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
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

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
    ) {
        KamelImage(
            resource = avatarResource,
            contentDescription = "${account.displayNameOrAcct}'s profile picture",
            contentScale = ContentScale.Inside,
            crossfade = true,
            animationSpec = tween()
        )
    }
}
