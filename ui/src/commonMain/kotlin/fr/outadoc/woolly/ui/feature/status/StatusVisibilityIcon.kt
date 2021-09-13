package fr.outadoc.woolly.ui.feature.status

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.StatusVisibility

@Composable
fun StatusVisibilityIcon(
    modifier: Modifier = Modifier,
    visibility: StatusVisibility
) {
    when (visibility) {
        StatusVisibility.Public -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.Public,
            contentDescription = "Public"
        )
        StatusVisibility.Unlisted -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.LockOpen,
            contentDescription = "Unlisted"
        )
        StatusVisibility.Private -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.Lock,
            contentDescription = "Followers-only"
        )
        StatusVisibility.Direct -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.Mail,
            contentDescription = "Direct"
        )
    }
}
