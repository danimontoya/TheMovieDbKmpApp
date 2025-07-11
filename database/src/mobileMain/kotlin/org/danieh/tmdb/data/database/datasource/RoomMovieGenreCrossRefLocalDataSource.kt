package org.danieh.tmdb.data.database.datasource

import org.danieh.tmdb.data.database.dao.MovieGenreCrossRefDao
import org.danieh.tmdb.data.database.model.MovieGenreCrossRefEntity
import org.danieh.tmdb.domain.datasource.MovieGenreCrossRefLocalDataSource

class RoomMovieGenreCrossRefLocalDataSource(
    private val dao: MovieGenreCrossRefDao
) : MovieGenreCrossRefLocalDataSource {

    override suspend fun insertMovieGenreIds(movieId: Int, genreId: Int) {
        val movieGenreCrossRefEntity = MovieGenreCrossRefEntity(movieId, genreId)
        dao.insertMovieGenreIds(movieGenreCrossRefEntity)
    }
}
