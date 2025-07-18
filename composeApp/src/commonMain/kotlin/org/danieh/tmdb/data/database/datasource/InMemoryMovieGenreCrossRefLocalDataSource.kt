package org.danieh.tmdb.data.database.datasource

import kotlinx.coroutines.flow.MutableStateFlow
import org.danieh.tmdb.domain.datasource.MovieGenreCrossRefLocalDataSource

class InMemoryMovieGenreCrossRefLocalDataSource : MovieGenreCrossRefLocalDataSource {

    private val _movieGenres: MutableStateFlow<Map<Int, List<Int>>> by lazy {
        MutableStateFlow(emptyMap())
    }

    override suspend fun insertMovieGenreIds(movieId: Int, genreId: Int) {
        val currentGenres = _movieGenres.value[movieId].orEmpty()
        _movieGenres.value += (movieId to (currentGenres + genreId))
    }

    suspend fun getGenreIdsByMovie(movieId: Int): List<Int> = _movieGenres.value[movieId].orEmpty()
}
