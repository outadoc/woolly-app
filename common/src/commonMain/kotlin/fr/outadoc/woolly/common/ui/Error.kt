package fr.outadoc.woolly.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LazyItemScope.CenteredErrorMessage(error: Throwable? = null, onRetry: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillParentMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ErrorMessage(
            error = error,
            onRetry = onRetry
        )
    }
}

@Composable
fun ErrorMessage(error: Throwable? = null, onRetry: () -> Unit = {}) {
    Text(
        "An error happened while loading.",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h5
    )

    error?.message?.let { errorMessage ->
        Text(
            errorMessage,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1
        )
    }

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = { onRetry() }
    ) {
        Text("Retry")
    }
}
