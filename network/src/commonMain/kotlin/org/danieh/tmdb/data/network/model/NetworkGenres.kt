package org.danieh.tmdb.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.danieh.tmdb.domain.model.Genre

@Serializable
data class NetworkGenres(
    @SerialName("genres")
    val genres: List<NetworkGenre>
)

@Serializable
data class NetworkGenre(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)

fun NetworkGenres.toDomain(): List<Genre> =
    genres.map { it.toDomain() }

internal fun NetworkGenre.toDomain() =
    Genre(
        id = id,
        name = name
    )
