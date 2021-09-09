package fr.outadoc.woolly.android

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.platform.UriHandler


class CustomTabUriHandler(private val context: Context) : UriHandler {

    private val intent = CustomTabsIntent.Builder().build()

    override fun openUri(uri: String) {
        intent.launchUrl(context, Uri.parse(uri))
    }
}
