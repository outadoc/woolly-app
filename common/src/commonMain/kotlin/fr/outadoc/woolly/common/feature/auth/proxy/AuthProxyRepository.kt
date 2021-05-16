package fr.outadoc.woolly.common.feature.auth.proxy

import fr.outadoc.mastodonk.api.entity.Token
import io.ktor.http.*

interface AuthProxyRepository {
    fun getAuthorizeUrl(domain: String): Url
    suspend fun getToken(domain: String, code: String): Token
}
