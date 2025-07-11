package org.danieh.tmdb.data.network.model


import arrow.core.Either
import arrow.core.raise.either
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.danieh.tmdb.domain.model.Video
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class NetworkVideos(
    @SerialName("id")
    val id: Int,
    @SerialName("results")
    val results: List<NetworkVideo>
)

@Serializable
data class NetworkVideo(
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("iso_3166_1")
    val iso31661: String,
    @SerialName("name")
    val name: String,
    @SerialName("key")
    val key: String,
    @SerialName("site")
    val site: String,
    @SerialName("size")
    val size: Int,
    @SerialName("type")
    val type: String,
    @SerialName("official")
    val official: Boolean,
    @SerialName("published_at")
    val publishedAt: String,
    @SerialName("id")
    val id: String
)

fun NetworkVideos.toDomain(): Either<Throwable, List<Video>> =
    either { results.map { it.toDomain().bind() } }

@OptIn(ExperimentalTime::class)
internal fun NetworkVideo.toDomain(): Either<Throwable, Video> =
    either {
        Video(
            iso6391 = iso6391,
            iso31661 = iso31661,
            name = name,
            key = key,
            site = site,
            size = size,
            type = type,
            official = official,
            publishedAt = Either.catch { Instant.parse(publishedAt) }.bind(),
            id = id
        )
    }
