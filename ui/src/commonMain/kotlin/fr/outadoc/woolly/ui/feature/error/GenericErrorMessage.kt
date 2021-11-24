package fr.outadoc.woolly.ui.feature.error

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GenericErrorMessage(message: String) {
    Text(
        message,
        modifier = Modifier.padding(top = 8.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge
    )
}
