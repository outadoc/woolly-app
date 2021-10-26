package fr.outadoc.woolly.ui.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.auth.component.CodeInputComponent
import fr.outadoc.woolly.common.feature.auth.component.CodeInputComponent.Event
import fr.outadoc.woolly.common.feature.auth.state.UserCredentials
import kotlinx.coroutines.flow.collect
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun CodeInputScreen(
    modifier: Modifier = Modifier,
    component: CodeInputComponent,
    domain: String,
    onSuccessfulAuthentication: (UserCredentials) -> Unit = {}
) {
    val state by component.state.collectAsState()

    LaunchedEffect(component.events) {
        component.events.collect { event ->
            when (event) {
                is Event.Authenticated -> onSuccessfulAuthentication(event.credentials)
            }
        }
    }

    var authCode by remember { mutableStateOf("") }
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.7f)) {
            Text(
                stringResource(MR.strings.onboarding_codeInput_title),
                style = MaterialTheme.typography.h4
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                label = { Text(stringResource(MR.strings.onboarding_codeInput_input_label)) },
                value = authCode,
                onValueChange = { value -> authCode = value },
                keyboardActions = KeyboardActions {
                    component.onAuthCodeReceived(domain, authCode)
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
                    text = error.message ?: stringResource(MR.strings.onboarding_codeInput_genericError_message),
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
                            component.getAuthorizeUrl(domain).toString()
                        )
                    }
                ) {
                    Text(stringResource(MR.strings.onboarding_codeInput_renewCode_action))
                }

                Button(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 32.dp)
                        .fillMaxWidth(),
                    onClick = {
                        component.onAuthCodeReceived(domain, authCode)
                    }
                ) {
                    Text(stringResource(MR.strings.onboarding_codeInput_submit_action))
                }
            }
        }
    }
}
