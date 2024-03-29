package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.account.component.MyAccountComponent
import fr.outadoc.woolly.ui.feature.preference.PreferenceList
import fr.outadoc.woolly.ui.feature.status.StatusList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyAccountScreen(
    component: MyAccountComponent,
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {},
    onLoadError: (Throwable, () -> Unit) -> Unit = { _, _ -> }
) {
    val state by component.state.collectAsState()
    val settingsState by component.settingsState.collectAsState()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            PreferenceList(
                modifier = Modifier.padding(insets),
                settingsState = settingsState,
                onUpdateSettingsState = component::onUpdateSettingsState
            )
        }
    ) {
        StatusList(
            modifier = Modifier.padding(insets),
            statusFlow = component.timelinePagingItems,
            lazyListState = component.listState,
            header = {
                state.account?.let { account ->
                    AccountHeader(
                        modifier = Modifier.padding(bottom = 8.dp),
                        account = account
                    )
                }
            },
            onStatusAction = { action ->
                component.onStatusAction(action)
            },
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onStatusReplyClick = onStatusReplyClick,
            onAccountClick = onAccountClick,
            onLoadError = onLoadError
        )
    }
}
