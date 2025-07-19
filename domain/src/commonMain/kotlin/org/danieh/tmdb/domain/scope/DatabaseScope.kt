package org.danieh.tmdb.domain.scope

import org.danieh.tmdb.domain.datasource.GenreLocalDataSource
import org.danieh.tmdb.domain.datasource.MovieGenreCrossRefLocalDataSource
import org.danieh.tmdb.domain.datasource.MovieLocalDataSource

interface DatabaseScope {
    val movieLocalDataSource: MovieLocalDataSource
    val movieGenreCrossRefLocalDataSource: MovieGenreCrossRefLocalDataSource
    val genreLocalDataSource: GenreLocalDataSource

    companion object {
        operator fun invoke(
            movieLocalDataSource: MovieLocalDataSource,
            movieGenreCrossRefLocalDataSource: MovieGenreCrossRefLocalDataSource,
            genreLocalDataSource: GenreLocalDataSource
        ) = object : DatabaseScope {
            override val movieLocalDataSource: MovieLocalDataSource by lazy { movieLocalDataSource }
            override val movieGenreCrossRefLocalDataSource: MovieGenreCrossRefLocalDataSource by lazy { movieGenreCrossRefLocalDataSource }
            override val genreLocalDataSource: GenreLocalDataSource by lazy { genreLocalDataSource }
        }
    }
}
