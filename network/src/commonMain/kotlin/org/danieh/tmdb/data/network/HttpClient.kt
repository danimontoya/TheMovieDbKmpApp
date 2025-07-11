package org.danieh.tmdb.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.headers

val httpClient: HttpClient by lazy {
    HttpClient {
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
