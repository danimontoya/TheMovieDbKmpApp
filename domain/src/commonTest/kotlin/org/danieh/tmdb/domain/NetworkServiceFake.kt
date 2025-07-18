package org.danieh.tmdb.domain

import arrow.core.Either
import org.danieh.tmdb.domain.model.Cast
import org.danieh.tmdb.domain.model.Genre
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.domain.model.MovieDetails
import org.danieh.tmdb.domain.model.Video

internal open class NetworkServiceFake : NetworkService {
    override suspend fun getPopularMovies(): Either<NetworkError, List<Pair<Movie, List<Int>>>> =
        notImplemented()

    override suspend fun getMovieDetails(id: Int): Either<NetworkError, MovieDetails> =
        notImplemented()

    override suspend fun getVideos(id: Int): Either<NetworkError, List<Video>> =
        notImplemented()

    override suspend fun getCast(id: Int): Either<NetworkError, List<Cast>> =
        notImplemented()

    override suspend fun getGenres(): Either<NetworkError, List<Genre>> =
        notImplemented()

    private fun <T> notImplemented(): T = throw NotImplementedError("Stub not implemented")
}
