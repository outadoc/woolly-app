package fr.outadoc.woolly.common.feature.auth.info

import fr.outadoc.mastodonk.api.entity.Token
import kotlinx.serialization.Serializable

@Serializable
data class AuthInfo(val domain: String, val token: Token)
