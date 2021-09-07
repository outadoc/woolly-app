package fr.outadoc.woolly.ui.feature.composer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.composer.InReplyToStatusPayload
import fr.outadoc.woolly.common.feature.composer.component.ComposerComponent
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import fr.outadoc.woolly.ui.feature.status.StatusOrBoost
import org.kodein.di.compose.instance

@Composable
fun ComposerScreen(
    component: ComposerComponent,
    inReplyToStatusPayload: InReplyToStatusPayload?,
    onDismiss: () -> Unit = {}
) {
    val accountRepository by instance<AccountRepository>()

    val account by accountRepository.currentAccount.collectAsState()
    val state by component.state.collectAsState()

    inReplyToStatusPayload?.let { payload ->
        LaunchedEffect(payload) {
            component.loadStatusRepliedTo(
                acct = payload.acct,
                statusId = payload.statusId
            )
        }
    }

    ComposerAndParentStatus(
        message = state.message,
        account = account,
        isLoadingParentStatus = state.isLoading,
        inReplyToStatus = state.inReplyToStatus,
        onMessageChange = { message ->
            component.onMessageChange(message)
        },
        onSubmit = {
            component.onSubmit()
            onDismiss()
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ComposerAndParentStatus(
    modifier: Modifier = Modifier,
    message: String,
    account: Account? = null,
    isLoadingParentStatus: Boolean,
    inReplyToStatus: Status?,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        state = rememberLazyListState(
            // Skip first item (the header)
            initialFirstVisibleItemIndex = 1
        )
    ) {
        item {
            when {
                isLoadingParentStatus -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
                inReplyToStatus != null -> {
                    StatusOrBoost(
                        modifier = Modifier.padding(bottom = 16.dp),
                        status = inReplyToStatus
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.fillParentMaxHeight()) {
                AnimatedVisibility(
                    modifier = Modifier.padding(bottom = 8.dp),
                    visible = inReplyToStatus != null
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Reply,
                            contentDescription = "Replying to"
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = "In reply to ${inReplyToStatus?.account?.displayNameOrAcct}",
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }

                ComposerBox(
                    modifier = Modifier.padding(top = 8.dp),
                    message = message,
                    account = account,
                    onMessageChange = onMessageChange,
                    onSubmit = onSubmit
                )
            }
        }
    }
}

@Composable
private fun ComposerBox(
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

            Button(onClick = onSubmit) {
                Text("Send")
            }
        }
    }
}
