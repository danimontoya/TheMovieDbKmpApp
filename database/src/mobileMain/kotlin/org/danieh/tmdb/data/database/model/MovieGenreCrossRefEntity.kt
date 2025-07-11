package org.danieh.tmdb.data.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "movie_genre_cross_ref",
    primaryKeys = ["movieId", "genreId"],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["movieId"],
            childColumns = ["movieId"]
        ),
        ForeignKey(
            entity = GenreEntity::class,
            parentColumns = ["genreId"],
            childColumns = ["genreId"]
        )
    ],
    indices = [
        Index("movieId"),
        Index("genreId"),
    ]
)
data class MovieGenreCrossRefEntity(
    val movieId: Int,
    val genreId: Int
)
