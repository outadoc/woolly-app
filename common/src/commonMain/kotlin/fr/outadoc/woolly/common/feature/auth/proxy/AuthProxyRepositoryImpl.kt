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
        val encodedDomain = domain.trim().encodeURLPath()
        return Url(baseUrl).copy(
            encodedPath = "oauth/$encodedDomain/authorize",
            parameters = parametersOf(
                "redirect_uri" to listOf(redirectUri),
                "scope" to listOf(scope),
            )
        )
    }

    override suspend fun getToken(domain: String, code: String): Token {
        val encodedDomain = domain.trim().encodeURLPath()
        val url = Url(baseUrl).copy(
            encodedPath = "oauth/$encodedDomain/token"
        )

        return withContext(Dispatchers.IO) {
            httpClient.post(url) {
                body = FormDataContent(
                    formData = parametersOf(
                        "redirect_uri" to listOf(redirectUri),
                        "scope" to listOf(scope),
                        "code" to listOf(code)
                    )
                )
            }
        }
    }
}
