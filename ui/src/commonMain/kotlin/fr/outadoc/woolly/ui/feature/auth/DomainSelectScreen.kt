package fr.outadoc.woolly.ui.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.auth.component.DomainSelectComponent
import fr.outadoc.woolly.common.feature.auth.component.DomainSelectComponent.Event
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource
import kotlinx.coroutines.flow.collect

@Composable
fun DomainSelectScreen(
    modifier: Modifier = Modifier,
    component: DomainSelectComponent,
    onDomainSelected: (String) -> Unit = {}
) {
    val state by component.state.collectAsState()

    LaunchedEffect(component.events) {
        component.events.collect { event ->
            when (event) {
                is Event.DomainSelectedEvent -> onDomainSelected(event.domain)
            }
        }
    }

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
                stringResource(MR.strings.onboardingDomain_title),
                style = MaterialTheme.typography.titleSmall
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                label = { Text(stringResource(MR.strings.onboardingDomain_input_label)) },
                placeholder = { Text(stringResource(MR.strings.onboardingDomain_input_placeholder)) },
                value = state.domain,
                onValueChange = { domain -> component.onDomainTextChanged(domain) },
                keyboardActions = KeyboardActions {
                    component.onSubmitDomain()
                },
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

            when (val error = state.error) {
                null -> {
                }
                else -> Text(
                    text = error.message
                        ?: stringResource(MR.strings.onboardingDomain_genericError_message),
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = { component.onSubmitDomain() }
            ) {
                Text(stringResource(MR.strings.onboardingDomain_submit_action))
            }
        }
    }
}
