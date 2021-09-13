package fr.outadoc.woolly.ui.feature.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    error: Throwable? = null,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "An error happened while loading.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5
        )

        ExceptionErrorMessage(error)

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = { onRetry() }
        ) {
            Text("Retry")
        }
    }
}
