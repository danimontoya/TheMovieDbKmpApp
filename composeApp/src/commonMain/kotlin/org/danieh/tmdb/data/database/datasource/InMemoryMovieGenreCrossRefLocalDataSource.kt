package org.danieh.tmdb.data.database.datasource

import org.danieh.tmdb.domain.datasource.MovieGenreCrossRefLocalDataSource

class InMemoryMovieGenreCrossRefLocalDataSource : MovieGenreCrossRefLocalDataSource {

    override suspend fun insertMovieGenreIds(movieId: Int, genreId: Int) = Unit
}
