package fr.outadoc.woolly.desktop

import androidx.compose.ui.platform.UriHandler
import java.awt.Desktop
import java.net.URI

class DesktopUriHandler : UriHandler {

    private val desktop = Desktop.getDesktop()

    override fun openUri(uri: String) {
        if (Desktop.isDesktopSupported()) {
            desktop.browse(URI(uri))
        }
    }
}
