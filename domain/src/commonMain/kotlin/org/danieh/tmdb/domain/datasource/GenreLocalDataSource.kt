package org.danieh.tmdb.domain.datasource

import org.danieh.tmdb.domain.model.Genre
import kotlinx.coroutines.flow.Flow

interface GenreLocalDataSource {
    suspend fun insertGenres(genres: List<Genre>)

    fun getAllGenres(): Flow<List<Genre>>

    suspend fun getGenreById(genreId: Int): Genre?
}
