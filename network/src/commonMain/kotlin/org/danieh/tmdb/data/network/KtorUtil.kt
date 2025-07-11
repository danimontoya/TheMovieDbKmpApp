package org.danieh.tmdb.data.network

import arrow.core.Either
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.util.date.GMTDate
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

const val baseUrl = "https://api.themoviedb.org/3/"
const val pictureBaseUrl = "https://image.tmdb.org/t/p/original"

suspend inline fun HttpClient.safeGet(
    path: String,
    requestBuilder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = try {
    get(urlString = baseUrl + path, block = requestBuilder)
} catch (e: Exception) {
    println(e.failureReason())
    throw e
}

suspend inline fun <reified T> HttpResponse.safeReceive(
    serializer: KSerializer<T>,
    json: Json = sharedJson
) = Either.catch {
    val jsonString = bodyAsText()

    val jsonElement = json.parseToJsonElement(jsonString)

    json.decodeFromJsonElement(serializer, jsonElement)
}

fun Throwable.failureReason(): String = "Network exception: $message"

@OptIn(ExperimentalSerializationApi::class)
val sharedJson: Json by lazy {
    Json {
        isLenient = true
        ignoreUnknownKeys = true
        allowTrailingComma = true
        encodeDefaults = true
    }
}
