package org.danieh.tmdb.domain.datasource

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import org.danieh.tmdb.domain.model.Genre

class InMemoryGenreLocalDataSource : GenreLocalDataSource {

    private val _genres: MutableStateFlow<List<Genre>> by lazy { MutableStateFlow(emptyList()) }

    override suspend fun insertGenres(genres: List<Genre>) {
        _genres.value = genres
    }

    override suspend fun getGenreById(genreId: Int): Genre? =
        _genres.firstOrNull()
            ?.firstOrNull {
                it.id == genreId
            }
}
