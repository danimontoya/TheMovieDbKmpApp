package org.danieh.tmdb.data.network.model

import arrow.core.Either
import arrow.core.raise.either
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.danieh.tmdb.domain.model.Cast

@Serializable
data class NetworkCredits(
    @SerialName("id")
    val id: Int,
    @SerialName("cast")
    val networkCast: List<NetworkCast>,
    @SerialName("crew")
    val networkCrew: List<NetworkCrew>
)

@Serializable
data class NetworkCrew(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("gender")
    val gender: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("department")
    val department: String,
    @SerialName("job")
    val job: String
)

@Serializable
data class NetworkCast(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("gender")
    val gender: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("cast_id")
    val castId: Int,
    @SerialName("character")
    val character: String,
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("order")
    val order: Int
)

fun NetworkCredits.toDomain(): Either<Throwable, List<Cast>> =
    either {
        networkCast.map {
            it.toDomain().bind()
        }
    }

internal fun NetworkCast.toDomain(): Either<Throwable, Cast> =
    either {
        Cast(
            adult = adult,
            gender = gender,
            id = id,
            knownForDepartment = knownForDepartment,
            name = name,
            originalName = originalName,
            popularity = popularity,
            profilePath = profilePath,
            castId = castId,
            character = character,
            creditId = creditId,
            order = order
        )
    }
