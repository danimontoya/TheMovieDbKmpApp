package org.danieh.tmdb.data.network.model

import arrow.core.Either
import arrow.core.raise.either
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.danieh.tmdb.domain.model.MovieDetails
import org.danieh.tmdb.domain.model.ProductionCompany
import org.danieh.tmdb.domain.model.ProductionCountry
import org.danieh.tmdb.domain.model.SpokenLanguage

@Serializable
data class NetworkMovieDetail(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("belongs_to_collection")
    val belongsToCollection: String?,
    @SerialName("budget")
    val budget: Int,
    @SerialName("genres")
    val genres: List<NetworkGenre>,
    @SerialName("homepage")
    val homepage: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("imdb_id")
    val imdbId: String?,
    @SerialName("origin_country")
    val originCountry: List<String>,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("production_companies")
    val productionCompanies: List<NetworkProductionCompany>,
    @SerialName("production_countries")
    val productionCountries: List<NetworkProductionCountry>,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("revenue")
    val revenue: Int,
    @SerialName("runtime")
    val runtime: Int?,
    @SerialName("spoken_languages")
    val spokenLanguages: List<NetworkSpokenLanguage>,
    @SerialName("status")
    val status: String,
    @SerialName("tagline")
    val tagline: String?,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
)

@Serializable
data class NetworkProductionCompany(
    @SerialName("id")
    val id: Int,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("name")
    val name: String,
    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class NetworkProductionCountry(
    @SerialName("iso_3166_1")
    val iso31661: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class NetworkSpokenLanguage(
    @SerialName("english_name")
    val englishName: String,
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("name")
    val name: String
)

internal fun NetworkProductionCompany.toDomain(): ProductionCompany =
    ProductionCompany(
        id = id,
        logoPath = logoPath,
        name = name,
        originCountry = originCountry
    )

internal fun NetworkProductionCountry.toDomain(): ProductionCountry =
    ProductionCountry(
        iso31661 = iso31661,
        name = name
    )

internal fun NetworkSpokenLanguage.toDomain(): SpokenLanguage =
    SpokenLanguage(
        englishName = englishName,
        iso6391 = iso6391,
        name = name
    )

fun NetworkMovieDetail.toDomain(): Either<Throwable, MovieDetails> =
    either {
        MovieDetails(
            adult = adult,
            backdropPath = backdropPath,
            belongsToCollection = belongsToCollection,
            budget = budget,
            genres = genres.map { it.toDomain() },
            homepage = homepage,
            id = id,
            imdbId = imdbId,
            originCountry = originCountry,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            productionCompanies = productionCompanies.map { it.toDomain() },
            productionCountries = productionCountries.map { it.toDomain() },
            releaseDate = Either.catch { LocalDate.parse(releaseDate) }.bind(),
            revenue = revenue,
            runtime = runtime,
            spokenLanguages = spokenLanguages.map { it.toDomain() },
            status = status,
            tagline = tagline,
            title = title,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }
