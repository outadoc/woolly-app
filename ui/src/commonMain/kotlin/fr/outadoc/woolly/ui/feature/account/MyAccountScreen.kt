package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import fr.outadoc.woolly.common.feature.account.component.MyAccountComponent

@Composable
fun MyAccountScreen(
    component: MyAccountComponent,
    insets: PaddingValues = PaddingValues()
) {
    val state by component.state.collectAsState()
    val scrollState = rememberScrollState()

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
