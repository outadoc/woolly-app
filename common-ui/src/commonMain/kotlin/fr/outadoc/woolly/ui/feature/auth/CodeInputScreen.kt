package fr.outadoc.woolly.ui.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.auth.viewmodel.AuthViewModel
import org.kodein.di.compose.instance

@Composable
fun CodeInputScreen(state: AuthViewModel.State.InstanceSelected) {
    val viewModel by instance<AuthViewModel>()

    var authCode by remember { mutableStateOf("") }
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome to Woolly") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackPressed() }) {
                        Icon(Icons.Default.ArrowBack, "Go back")
                    }
                }
            )
        }
    ) { insets ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
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
                    keyboardActions = KeyboardActions {
                        viewModel.onAuthCodeReceived(authCode)
                    },
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

                when (val error = state.error) {
                    null -> {
                    }
                    else -> Text(
                        text = error.message ?: "Error while authenticating.",
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
                            viewModel.onAuthCodeReceived(authCode)
                        }
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}
