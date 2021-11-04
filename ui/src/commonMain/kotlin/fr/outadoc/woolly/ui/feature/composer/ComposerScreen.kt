package fr.outadoc.woolly.ui.feature.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.woolly.common.feature.composer.InReplyToStatusPayload
import fr.outadoc.woolly.common.feature.composer.component.ComposerComponent

@Composable
fun ComposerScreen(
    component: ComposerComponent,
    inReplyToStatusPayload: InReplyToStatusPayload?,
    onDismiss: () -> Unit = {}
) {
    inReplyToStatusPayload?.let { payload ->
        LaunchedEffect(payload) {
            component.loadStatusRepliedTo(
                statusId = payload.statusId
            )
        }
    }

    val state by component.state.collectAsState()

    ComposerAndParentStatus(
        message = state.message,
        account = state.currentAccount,
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
