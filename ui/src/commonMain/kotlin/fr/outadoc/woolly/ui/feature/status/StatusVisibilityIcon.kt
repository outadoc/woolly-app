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
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun StatusVisibilityIcon(
    modifier: Modifier = Modifier,
    visibility: StatusVisibility
) {
    when (visibility) {
        StatusVisibility.Public -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.Public,
            contentDescription = stringResource(MR.strings.status_visibility_public_cd)
        )
        StatusVisibility.Unlisted -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.LockOpen,
            contentDescription = stringResource(MR.strings.status_visibility_unlisted_cd)
        )
        StatusVisibility.Private -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.Lock,
            contentDescription = stringResource(MR.strings.status_visibility_private_cd)
        )
        StatusVisibility.Direct -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.Mail,
            contentDescription = stringResource(MR.strings.status_visibility_direct_cd)
        )
    }
}
