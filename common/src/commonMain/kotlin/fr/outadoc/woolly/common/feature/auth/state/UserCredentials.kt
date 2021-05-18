package fr.outadoc.woolly.common.feature.auth.state

import fr.outadoc.mastodonk.api.entity.Token
import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(val domain: String, val token: Token)
