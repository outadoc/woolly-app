package fr.outadoc.woolly.common.feature.auth.state

interface AuthenticationStateConsumer {
    suspend fun appendCredentials(credentials: UserCredentials)
    suspend fun logoutAll()
}
