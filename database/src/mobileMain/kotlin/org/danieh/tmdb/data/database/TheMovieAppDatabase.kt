package org.danieh.tmdb.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import org.danieh.tmdb.data.database.dao.GenreDao
import org.danieh.tmdb.data.database.dao.MovieDao
import org.danieh.tmdb.data.database.dao.MovieGenreCrossRefDao
import org.danieh.tmdb.data.database.model.GenreEntity
import org.danieh.tmdb.data.database.model.MovieEntity
import org.danieh.tmdb.data.database.model.MovieGenreCrossRefEntity

@Database(
    entities = [MovieEntity::class, GenreEntity::class, MovieGenreCrossRefEntity::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
@ConstructedBy(TheMovieAppDatabaseConstructor::class)
abstract class TheMovieAppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun movieGenreCrossRefDao(): MovieGenreCrossRefDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object TheMovieAppDatabaseConstructor : RoomDatabaseConstructor<TheMovieAppDatabase> {
    override fun initialize(): TheMovieAppDatabase
}

