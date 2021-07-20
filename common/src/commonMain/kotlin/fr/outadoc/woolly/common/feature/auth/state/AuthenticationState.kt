package fr.outadoc.woolly.common.feature.auth.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AuthenticationState(
    @SerialName("accounts")
    val accounts: List<UserCredentials> = emptyList()
) {
    @Transient
    val activeAccount: UserCredentials? = accounts.firstOrNull()
}
