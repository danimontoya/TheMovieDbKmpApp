package org.danieh.tmdb.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val httpClient: HttpClient by lazy {
    HttpClient {
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
        install(createClientPlugin("auth") {
            onRequest { request, _ ->
                request.headers {
                    append(name = "accept", value = "application/json")
                    append(
                        name = "Authorization",
                        value = "Bearer eyJh..blablabla - Add your key here"
                    )
                }.build()
            }
        })
    }
}
