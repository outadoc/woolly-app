package fr.outadoc.woolly.ui.feature.composer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.composer.viewmodel.ComposerViewModel
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import org.kodein.di.compose.instance

@Composable
fun ComposerScreen(
    onDismiss: () -> Unit
) {
    val viewModel by instance<ComposerViewModel>()
    val accountRepository by instance<AccountRepository>()

    val account by accountRepository.currentAccount.collectAsState()
    val state by viewModel.state.collectAsState()

    when (val state = state) {
        is ComposerViewModel.State.Composing -> {
            Composer(
                modifier = Modifier.padding(16.dp),
                message = state.message,
                account = account,
                onMessageChange = { message ->
                    viewModel.onMessageChange(message)
                },
                onSubmit = {
                    viewModel.onSubmit()
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun Composer(
    modifier: Modifier = Modifier,
    message: String,
    account: Account? = null,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit = {}
) {
    Column(modifier = modifier) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = message,
            onValueChange = onMessageChange,
            placeholder = { Text("What's on your mind?") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f, fill = true),
                verticalAlignment = Alignment.CenterVertically
            ) {
                account?.let { account ->
                    ProfilePicture(
                        modifier = Modifier.padding(end = 8.dp),
                        size = 24.dp,
                        account = account
                    )

                    Text(
                        buildAnnotatedString {
                            append("Posting as ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(account.displayNameOrAcct)
                            }
                        }
                    )
                }
            }

            Button(
                onClick = onSubmit
            ) {
                Text("Send")
            }
        }
    }
}
