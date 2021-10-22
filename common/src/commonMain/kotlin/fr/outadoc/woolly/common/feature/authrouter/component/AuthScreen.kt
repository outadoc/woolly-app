package fr.outadoc.woolly.common.feature.authrouter.component

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class AuthScreen : Parcelable {

    @Parcelize
    object AuthDomainSelect : AuthScreen()

    @Parcelize
    data class AuthCodeInput(val domain: String) : AuthScreen()
}
