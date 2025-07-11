package org.danieh.tmdb.domain

import arrow.core.Either
import org.danieh.tmdb.domain.model.Cast
import org.danieh.tmdb.domain.model.Genre
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.domain.model.MovieDetails
import org.danieh.tmdb.domain.model.Video

interface NetworkService {

    suspend fun getPopularMovies(): Either<NetworkError, List<Pair<Movie, List<Int>>>>

    suspend fun getMovieDetails(id: Int): Either<NetworkError, MovieDetails>

    suspend fun getVideos(id: Int): Either<NetworkError, List<Video>>

    suspend fun getCast(id: Int): Either<NetworkError, List<Cast>>

    suspend fun getGenres(): Either<NetworkError, List<Genre>>
}

sealed class NetworkError {
    data object DeserializationError : NetworkError()
    data object UnknownError : NetworkError()
}
