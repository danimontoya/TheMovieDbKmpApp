package org.danieh.tmdb.data.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.danieh.tmdb.domain.datasource.MovieLocalDataSource
import org.danieh.tmdb.domain.model.Movie

class InMemoryMovieLocalDataSource : MovieLocalDataSource {

    private val _movies: MutableStateFlow<List<Movie>> by lazy { MutableStateFlow(emptyList()) }

    override suspend fun insertMovies(movies: List<Movie>) {
        _movies.value = movies
    }

    override fun getAllMovies(): Flow<List<Movie>> =
        _movies.asStateFlow()

    override fun getMovieById(id: Int): Flow<Movie?> =
        _movies.asStateFlow()
            .map { list ->
                list.firstOrNull { it.id == id }
            }

    override suspend fun updateMovie(id: Int, runtime: Int) =
        _movies.update { movies ->
            movies.map { movie ->
                if (id == movie.id) movie.copy(runtime = runtime) else movie
            }
        }
}
