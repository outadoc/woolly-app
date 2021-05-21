package fr.outadoc.woolly.common.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.auth.viewmodel.AuthViewModel
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun CodeInputScreen(state: AuthViewModel.State.InstanceSelected) {
    val di = LocalDI.current
    val vm by di.instance<AuthViewModel>()

    var authCode by remember { mutableStateOf("") }
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome to Woolly") },
                navigationIcon = {
                    IconButton(onClick = { vm.onBackPressed() }) {
                        Icon(Icons.Default.ArrowBack, "Go back")
                    }
                }
            )
        }
    ) { insets ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                Text(
                    "Enter your authorization code",
                    style = MaterialTheme.typography.h4
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    label = { Text("Authorization code") },
                    value = authCode,
                    onValueChange = { value -> authCode = value },
                    keyboardActions = KeyboardActions(
                        onDone = { vm.onAuthCodeReceived(authCode) }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    trailingIcon = {
                        if (state.loading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    },
                    singleLine = true
                )

                if (state.error != null) {
                    Text(
                        text = state.error.message ?: "Error while authenticating.",
                        modifier = Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.error
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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

                    Button(
                        modifier = Modifier
                            .padding(top = 16.dp, start = 32.dp)
                            .fillMaxWidth(),
                        onClick = {
                            vm.onAuthCodeReceived(authCode)
                        }
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}
