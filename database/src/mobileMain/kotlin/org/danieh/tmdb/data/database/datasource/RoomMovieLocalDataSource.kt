package org.danieh.tmdb.data.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.danieh.tmdb.data.database.dao.MovieDao
import org.danieh.tmdb.data.database.model.toDatabase
import org.danieh.tmdb.data.database.model.toDomain
import org.danieh.tmdb.domain.datasource.MovieLocalDataSource
import org.danieh.tmdb.domain.model.Movie

class RoomMovieLocalDataSource(
    private val movieDao: MovieDao
) : MovieLocalDataSource {

    override suspend fun insertMovies(movies: List<Movie>) =
        movieDao.insertMovies(movies.map { it.toDatabase() })

    override fun getAllMovies(): Flow<List<Movie>> =
        movieDao.getAllMovies().map { moviesWithGenres -> moviesWithGenres.map { it.toDomain() } }

    override fun getMovieById(id: Int): Flow<Movie?> =
        movieDao.getMovieWithGenres(id).map { movieWithGenres -> movieWithGenres?.toDomain() }

    override suspend fun updateMovie(id: Int, runtime: Int) =
        movieDao.updateMovie(id, runtime)
}
