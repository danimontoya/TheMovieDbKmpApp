package org.danieh.tmdb.domain.datasource

import org.danieh.tmdb.domain.model.Genre

interface GenreLocalDataSource {
    suspend fun insertGenres(genres: List<Genre>)

    suspend fun getGenreById(genreId: Int): Genre?
}
