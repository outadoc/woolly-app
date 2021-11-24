package fr.outadoc.woolly.ui.feature.share

import android.content.Intent
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun ShareButton(
    modifier: Modifier,
    shareUrl: String?,
    shareTitle: String?,
    shareText: String?,
    icon: @Composable () -> Unit
) {
    val context = LocalContext.current
    val intent = Intent.createChooser(
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            shareTitle?.let { putExtra(Intent.EXTRA_TITLE, it) }
            shareUrl?.let { putExtra(Intent.EXTRA_TEXT, it) }
        },
        null
    )

    IconButton(
        modifier = modifier,
        onClick = { context.startActivity(intent) }
    ) {
        icon()
    }
}
