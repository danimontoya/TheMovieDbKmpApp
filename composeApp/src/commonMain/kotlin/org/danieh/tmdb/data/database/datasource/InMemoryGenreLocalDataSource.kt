package org.danieh.tmdb.data.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import org.danieh.tmdb.domain.datasource.GenreLocalDataSource
import org.danieh.tmdb.domain.model.Genre

class InMemoryGenreLocalDataSource : GenreLocalDataSource {

    private val _genres: MutableStateFlow<List<Genre>> by lazy { MutableStateFlow(emptyList()) }

    override suspend fun insertGenres(genres: List<Genre>) {
        _genres.value = genres
    }

    override fun getAllGenres(): Flow<List<Genre>> =
        _genres.asStateFlow()

    override suspend fun getGenreById(genreId: Int): Genre? =
        _genres.firstOrNull()
            ?.firstOrNull {
                it.id == genreId
            }
}
