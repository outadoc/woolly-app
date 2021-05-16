package fr.outadoc.woolly.common.feature.auth.proxy

import fr.outadoc.mastodonk.api.entity.Token
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthProxyRepositoryImpl(private val httpClient: HttpClient) : AuthProxyRepository {

    private val baseUrl = "https://api.woolly.fr"
    private val redirectUri = "urn:ietf:wg:oauth:2.0:oob"
    private val scope = "read write follow push"

    override fun getAuthorizeUrl(domain: String): Url {
        return URLBuilder(baseUrl).apply {
            encodedPath = "oauth/${domain.trim()}/authorize"
            parameters.apply {
                append("redirect_uri", redirectUri)
                append("scope", scope)
            }
        }.build()
    }

    override suspend fun getToken(domain: String, code: String): Token {
        return withContext(Dispatchers.IO) {
            val url = URLBuilder(baseUrl).apply {
                encodedPath = "oauth/${domain.trim()}/token"
            }.build()

            httpClient.post(url) {
                body = FormDataContent(
                    Parameters.build {
                        append("redirect_uri", redirectUri)
                        append("scope", scope)
                        append("code", code)
                    }
                )
            }
        }
    }
}
