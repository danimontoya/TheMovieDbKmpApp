package org.danieh.tmdb.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import org.danieh.tmdb.domain.model.Genre
import org.danieh.tmdb.domain.model.Movie

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val movieId: Int,
    val adult: Boolean,
    val backdropPath: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: LocalDate,
    val runtime: Int?,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)

fun MovieWithGenres.toDomain() =
    Movie(
        id = movie.movieId,
        adult = movie.adult,
        backdropPath = movie.backdropPath,
        genres = genres.toDomain(),
        originalLanguage = movie.originalLanguage,
        originalTitle = movie.originalTitle,
        overview = movie.overview,
        popularity = movie.popularity,
        posterPath = movie.posterPath,
        releaseDate = movie.releaseDate,
        runtime = movie.runtime,
        title = movie.title,
        video = movie.video,
        voteAverage = movie.voteAverage,
        voteCount = movie.voteCount,
    )

fun List<GenreEntity>.toDomain(): List<Genre> =
    map { genreEntity ->
        Genre(id = genreEntity.genreId, name = genreEntity.name)
    }

fun Movie.toDatabase() =
    MovieEntity(
        adult = adult,
        backdropPath = backdropPath,
        movieId = id,
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
