package fr.outadoc.woolly.common.feature.auth.proxy

import fr.outadoc.mastodonk.api.entity.Token
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.encodeURLPath
import io.ktor.http.encodedPath
import io.ktor.http.parameters
import io.ktor.http.parametersOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthProxyRepositoryImpl(private val httpClient: HttpClient) : AuthProxyRepository {

    private val baseUrl = "https://api.woolly.fr"
    private val redirectUri = "urn:ietf:wg:oauth:2.0:oob"
    private val scope = "read write follow push"

    override fun getAuthorizeUrl(domain: String): Url {
        val encodedDomain = domain.trim().encodeURLPath()
        return URLBuilder(baseUrl).apply {
            encodedPath = "oauth/$encodedDomain/authorize"
            parameters {
                append("redirect_uri", redirectUri)
                append("scope", scope)
            }
        }.build()
    }

    override suspend fun getToken(domain: String, code: String): Token {
        val encodedDomain = domain.trim().encodeURLPath()
        val url = URLBuilder(baseUrl).apply {
            encodedPath = "oauth/$encodedDomain/token"
        }.build()

        return withContext(Dispatchers.IO) {
            httpClient.post(url) {
                setBody(
                    FormDataContent(
                        formData = parametersOf(
                            "redirect_uri" to listOf(redirectUri),
                            "scope" to listOf(scope),
                            "code" to listOf(code)
                        )
                    )
                )
            }.body()
        }
    }
}
