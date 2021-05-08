import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import fr.outadoc.woolly.common.App

fun main() = Window(
    title = "Mastodon Compose",
    size = IntSize(width = 500, height = 750)
) {
    App()
}
