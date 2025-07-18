package org.danieh.tmdb.domain.datasource

import kotlinx.coroutines.flow.MutableStateFlow

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
