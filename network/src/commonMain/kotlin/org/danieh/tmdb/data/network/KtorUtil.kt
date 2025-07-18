package org.danieh.tmdb.data.network

import arrow.core.Either
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

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

suspend inline fun <reified T> HttpResponse.safeReceive() = Either.catch { body<T>() }

fun Throwable.failureReason(): String = "Network exception: $message"
