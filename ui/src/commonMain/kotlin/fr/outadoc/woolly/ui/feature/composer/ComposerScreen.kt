package fr.outadoc.woolly.ui.feature.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.composer.InReplyToStatusPayload
import fr.outadoc.woolly.common.feature.composer.component.ComposerComponent
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
