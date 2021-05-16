package fr.outadoc.woolly.common.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.auth.AuthState
import fr.outadoc.woolly.common.feature.auth.AuthViewModel
import fr.outadoc.woolly.common.plus
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun DomainSelectScreen(state: AuthState.Disconnected) {
    val di = LocalDI.current
    val vm by di.instance<AuthViewModel>()

    var domain by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Welcome to Woolly") })
        }
    ) { insets ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets + 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                Text(
                    "Choose your Mastodon instance",
                    style = MaterialTheme.typography.h4
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    label = { Text("Instance domain") },
                    placeholder = { Text("mastodon.example") },
                    value = domain,
                    onValueChange = { value -> domain = value },
                    keyboardActions = KeyboardActions(
                        onDone = { vm.onDomainSelected(domain) }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Uri
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
                        text = state.error.message ?: "Error while fetching instance details.",
                        modifier = Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.error
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        vm.onDomainSelected(domain)
                    }
                ) {
                    Text("Continue")
                }
            }
        }
    }
}
