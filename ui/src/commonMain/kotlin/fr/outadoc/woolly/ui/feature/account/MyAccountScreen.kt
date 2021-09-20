package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import fr.outadoc.woolly.common.feature.account.component.MyAccountComponent
import fr.outadoc.woolly.ui.feature.preference.PreferenceList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyAccountScreen(
    component: MyAccountComponent,
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    insets: PaddingValues = PaddingValues()
) {
    val state by component.state.collectAsState()
    val settingsState by component.settingsState.collectAsState()

    val scrollState = rememberScrollState()

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
        Box(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            state.account?.let { account ->
                AccountHeader(
                    modifier = Modifier.padding(insets),
                    account = account
                )
            }
        }
    }
}
