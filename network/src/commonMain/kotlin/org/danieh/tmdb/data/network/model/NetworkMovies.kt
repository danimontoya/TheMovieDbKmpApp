package org.danieh.tmdb.data.network.model

import arrow.core.Either
import arrow.core.raise.either
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.danieh.tmdb.domain.model.Movie

@Serializable
data class NetworkMovies(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<NetworkMovie>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class NetworkMovie(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    @SerialName("id")
    val id: Int,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
)

fun NetworkMovies.toDomain(): Either<Throwable, List<Pair<Movie, List<Int>>>> =
    either { results.map { it.toDomain().bind() } }

internal fun NetworkMovie.toDomain(): Either<Throwable, Pair<Movie, List<Int>>> =
    either {
        Pair(
            first = Movie(
                adult = adult,
                backdropPath = backdropPath,
                id = id,
                originalLanguage = originalLanguage,
                originalTitle = originalTitle,
                overview = overview,
                popularity = popularity,
                posterPath = posterPath,
                releaseDate = Either.catch { LocalDate.parse(releaseDate) }.bind(),
                runtime = null,
                title = title,
                video = video,
                voteAverage = voteAverage,
                voteCount = voteCount,
                genres = emptyList(),
            ),
            second = genreIds
        )
    }
