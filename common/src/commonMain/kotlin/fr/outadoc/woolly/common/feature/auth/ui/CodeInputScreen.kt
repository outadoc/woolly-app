package fr.outadoc.woolly.common.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.auth.AuthState
import fr.outadoc.woolly.common.feature.auth.AuthViewModel
import fr.outadoc.woolly.common.ui.ErrorScreen
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun CodeInputScreen(state: AuthState.InstanceSelected) {
    val di = LocalDI.current
    val vm by di.instance<AuthViewModel>()

    var authCode by remember { mutableStateOf("") }
    val uriHandler = LocalUriHandler.current

    if (state.loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            label = { Text("Enter the code") },
            placeholder = { Text("abcd1234") },
            value = authCode,
            onValueChange = { value -> authCode = value },
            keyboardActions = KeyboardActions(
                onDone = { vm.onAuthCodeReceived(authCode) }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Ascii
            ),
            singleLine = true
        )

        TextButton(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                uriHandler.openUri(
                    state.authorizeUrl.toString()
                )
            }
        ) {
            Text("Get a new code")
        }

        if (state.error != null) {
            Text(
                text = state.error.message ?: "Error while authenticating.",
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error
            )
        }

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                vm.onAuthCodeReceived(authCode)
            }
        ) {
            Text("Submit")
        }
    }
}