package fr.outadoc.woolly.common.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun AuthRouter() {
    val di = LocalDI.current
    val vm by di.instance<AuthViewModel>()
    val authState by vm.authState.collectAsState()


}
