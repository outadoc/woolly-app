package fr.outadoc.woolly.common.feature.auth.state

interface AuthenticationStateConsumer {

    fun logoutAll()
    fun appendCredentials(credentials: UserCredentials)
}
