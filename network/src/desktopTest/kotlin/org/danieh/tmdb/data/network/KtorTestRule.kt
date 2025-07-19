package org.danieh.tmdb.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.content.TextContent
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.junit.rules.ExternalResource

@OptIn(ExperimentalSerializationApi::class)
class KtorTestRule(
    private val baseUrl: String = "http://localhost/",
) : ExternalResource() {

    private var client: HttpClient? = null

    fun serverWithResponse(
        responseCode: Int,
        body: String = "",
        headers: Headers = headersOf(HttpHeaders.ContentType, Json.toString()),
        onRequest: (HttpRequestData) -> Unit = {}
    ): HttpClient {
        client?.close()
        client = HttpClient(MockEngine {
            onRequest(it)
            respond(
                content = body,
                status = HttpStatusCode.fromValue(responseCode),
                headers = headers
            )
        }) {
            expectSuccess = false
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        allowTrailingComma = true
                        encodeDefaults = true
                    }
                )
            }
            defaultRequest {
                url(baseUrl)
            }
        }
        return client!!
    }

    fun serverWithError(): HttpClient {
        client?.close()
        client = HttpClient(MockEngine {
            throw RuntimeException("Oops!")
        }) {
            expectSuccess = false
        }
        return client!!
    }

    override fun after() {
        client?.close()
    }
}

fun HttpRequestData.requireBodyAsText(): String =
    (body as? TextContent)?.text ?: error("Request body is not TextContent")