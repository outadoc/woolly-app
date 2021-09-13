package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.woolly.common.feature.composer.StatusPoster
import org.kodein.di.compose.instance

@Composable
fun PostingStatusSnackbar(
    showPostingSnackbar: () -> Unit,
    showErrorSnackbar: (() -> Unit) -> Unit
) {
    val statusPoster by instance<StatusPoster>()
    val state by statusPoster.state.collectAsState()

    when (state) {
        StatusPoster.State.Posting -> showPostingSnackbar()
        StatusPoster.State.Error -> showErrorSnackbar {
            statusPoster.retryAll()
        }
        else -> {
        }
    }
}
