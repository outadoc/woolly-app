package fr.outadoc.woolly.common.feature.auth.info

interface AuthInfoSubscriber {
    fun publish(authInfo: AuthInfo?)
}
