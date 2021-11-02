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
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource

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
            stringResource(MR.strings.all_genericError_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )

        ExceptionErrorMessage(error)

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = { onRetry() }
        ) {
            Text(stringResource(MR.strings.all_genericError_retry_action))
        }
    }
}
