package fr.outadoc.woolly.common.feature.authrouter.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.woolly.common.feature.auth.component.CodeInputComponent
import fr.outadoc.woolly.common.feature.auth.component.DomainSelectComponent

sealed class AuthContent {

    abstract val configuration: AuthScreen
    abstract val component: ComponentContext

    data class DomainSelect(
        override val configuration: AuthScreen.AuthDomainSelect,
        override val component: DomainSelectComponent
    ) : AuthContent()

    data class CodeInput(
        override val configuration: AuthScreen.AuthCodeInput,
        override val component: CodeInputComponent
    ) : AuthContent()
}