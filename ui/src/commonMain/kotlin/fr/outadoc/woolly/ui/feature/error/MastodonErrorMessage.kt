package fr.outadoc.woolly.ui.feature.error

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Error

@Composable
fun MastodonErrorMessage(apiError: Error) {
    Text(
        apiError.error,
        modifier = Modifier.padding(top = 8.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.body1
    )

    val description = apiError.errorDescription
    if (description != null) {
        Text(
            description,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body2
        )
    }
}
