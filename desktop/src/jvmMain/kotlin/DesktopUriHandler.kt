import androidx.compose.ui.platform.UriHandler
import java.awt.Desktop
import java.net.URI


class DesktopUriHandler : UriHandler {

    private val desktop = Desktop.getDesktop()

    override fun openUri(uri: String) {
        desktop.browse(URI(uri))
    }
}