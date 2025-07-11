package org.danieh.tmdb.domain.datasource

interface MovieGenreCrossRefLocalDataSource {
    suspend fun insertMovieGenreIds(movieId: Int, genreId: Int)
}
