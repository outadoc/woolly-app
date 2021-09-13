package fr.outadoc.woolly.ui.feature.error

import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.client.MastodonApiException

@Composable
fun ExceptionErrorMessage(error: Throwable?) {
    val genericErrorMessage = error?.message
    val apiError = (error as? MastodonApiException)?.apiError

    if (apiError != null) {
        MastodonErrorMessage(apiError)
    } else if (genericErrorMessage != null) {
        GenericErrorMessage(genericErrorMessage)
    }

}
