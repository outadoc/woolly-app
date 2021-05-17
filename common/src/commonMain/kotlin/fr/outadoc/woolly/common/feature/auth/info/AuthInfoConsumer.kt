package fr.outadoc.woolly.common.feature.auth.info

interface AuthInfoConsumer {
    fun publish(authInfo: AuthInfo?)
}
