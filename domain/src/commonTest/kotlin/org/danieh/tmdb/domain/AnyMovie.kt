package org.danieh.tmdb.domain

import kotlinx.datetime.LocalDate
import org.danieh.tmdb.domain.model.Genre
import org.danieh.tmdb.domain.model.Movie

internal fun anyMovie(
    id: Int,
    adult: Boolean = false,
    backdropPath: String = "/rDa3SfEijeRNCWtHQZCwfbGxYvR.jpg",
    genres: List<Genre> = emptyList(),
    originalLanguage: String = "en",
    originalTitle: String = "Kraven the Hunter",
    overview: String = "Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.",
    popularity: Double = 5481.159,
    posterPath: String = "/1GvBhRxY6MELDfxFrete6BNhBB5.jpg",
    releaseDate: LocalDate = LocalDate.parse("2024-12-11"),
    runtime: Int? = null,
    title: String = "Kraven the Hunter",
    video: Boolean = false,
    voteAverage: Double = 6.5,
    voteCount: Int = 671
) = Movie(
    id = id,
    adult = adult,
    backdropPath = backdropPath,
    genres = genres,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    runtime = runtime,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)
