package org.danieh.tmdb.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.danieh.tmdb.domain.model.Genre

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey
    val genreId: Int,
    val name: String
)

fun GenreEntity.toDomain() =
    Genre(
        id = genreId,
        name = name
    )

fun Genre.toDatabase() =
    GenreEntity(
        genreId = id,
        name = name
    )
