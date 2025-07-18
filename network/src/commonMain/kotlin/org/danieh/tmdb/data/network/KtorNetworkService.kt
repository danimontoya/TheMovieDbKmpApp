package org.danieh.tmdb.data.network

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import io.ktor.client.HttpClient
import org.danieh.tmdb.data.network.model.NetworkCredits
import org.danieh.tmdb.data.network.model.NetworkGenres
import org.danieh.tmdb.data.network.model.NetworkMovieDetail
import org.danieh.tmdb.data.network.model.NetworkMovies
import org.danieh.tmdb.data.network.model.NetworkVideos
import org.danieh.tmdb.data.network.model.toDomain
import org.danieh.tmdb.domain.NetworkError
import org.danieh.tmdb.domain.NetworkService
import org.danieh.tmdb.domain.model.Cast
import org.danieh.tmdb.domain.model.Genre
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.domain.model.MovieDetails
import org.danieh.tmdb.domain.model.Video

class KtorNetworkService(
    private val client: HttpClient
) : NetworkService {

    override suspend fun getPopularMovies(): Either<NetworkError, List<Pair<Movie, List<Int>>>> {
        val response = client.safeGet("movie/popular?language=en-US&page=1")

        return when (response.status.value) {
            200 -> response.safeReceive<NetworkMovies>()
                .flatMap { it.toDomain() }
                .mapLeft { NetworkError.DeserializationError }

            else -> NetworkError.UnknownError.left()
        }
    }

    override suspend fun getMovieDetails(id: Int): Either<NetworkError, MovieDetails> {
        val response = client.safeGet("movie/$id?language=en-US")

        return when (response.status.value) {
            200 -> response.safeReceive<NetworkMovieDetail>()
                .flatMap { it.toDomain() }
                .mapLeft { error -> NetworkError.DeserializationError.also { println(error.failureReason()) } }

            else -> NetworkError.UnknownError.left()
        }
    }

    override suspend fun getGenres(): Either<NetworkError, List<Genre>> {
        val response = client.safeGet("genre/movie/list?language=en")

        return when (response.status.value) {
            200 -> response.safeReceive<NetworkGenres>()
                .flatMap { it.toDomain().right() }
                .mapLeft { error -> NetworkError.DeserializationError.also { println(error.failureReason()) } }

            else -> NetworkError.UnknownError.left()
        }
    }

    override suspend fun getVideos(id: Int): Either<NetworkError, List<Video>> {
        val response = client.safeGet("movie/$id/videos?language=en-US")

        return when (response.status.value) {
            200 -> response.safeReceive<NetworkVideos>()
                .flatMap { it.toDomain() }
                .mapLeft { error -> NetworkError.DeserializationError.also { println(error.failureReason()) } }

            else -> NetworkError.UnknownError.left()
        }
    }

    override suspend fun getCast(id: Int): Either<NetworkError, List<Cast>> {
        val response = client.safeGet("movie/$id/credits?language=en-US")

        return when (response.status.value) {
            200 -> response.safeReceive<NetworkCredits>()
                .flatMap { it.toDomain() }
                .mapLeft { error -> NetworkError.DeserializationError.also { println(error.failureReason()) } }

            else -> NetworkError.UnknownError.left()
        }
    }
}
