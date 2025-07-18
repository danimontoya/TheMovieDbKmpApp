package org.danieh.tmdb.domain

import org.danieh.tmdb.domain.datasource.InMemoryGenreLocalDataSource
import org.danieh.tmdb.domain.datasource.InMemoryMovieGenreCrossRefLocalDataSource
import org.danieh.tmdb.domain.datasource.InMemoryMovieLocalDataSource
import org.danieh.tmdb.scope.DatabaseScope

internal class DatabaseScopeFake : DatabaseScope {
    override val genreLocalDataSource = InMemoryGenreLocalDataSource()
    override val movieGenreCrossRefLocalDataSource = InMemoryMovieGenreCrossRefLocalDataSource()
    override val movieLocalDataSource = InMemoryMovieLocalDataSource(
        genreLocalDataSource = genreLocalDataSource,
        movieGenreCrossRefLocalDataSource = movieGenreCrossRefLocalDataSource
    )

    suspend fun cleanUpMovies() = movieLocalDataSource.cleanUpMovies()
}
