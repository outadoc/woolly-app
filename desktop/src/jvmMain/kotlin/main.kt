import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import fr.outadoc.mastodonk.common.App

fun main() = Window(
    title = "Mastodon Compose",
    size = IntSize(width = 500, height = 700)
) {
    App()
}
