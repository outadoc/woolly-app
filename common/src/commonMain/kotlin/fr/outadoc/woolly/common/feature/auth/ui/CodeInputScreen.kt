package fr.outadoc.woolly.common.feature.auth.ui

import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.auth.AuthViewModel
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun CodeInputScreen(error: Throwable? = null) {
    val di = LocalDI.current
    val vm by di.instance<AuthViewModel>()

    
}