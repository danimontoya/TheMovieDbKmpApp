package org.danieh.tmdb.data.database.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.danieh.tmdb.data.database.dao.GenreDao
import org.danieh.tmdb.data.database.model.toDatabase
import org.danieh.tmdb.data.database.model.toDomain
import org.danieh.tmdb.domain.datasource.GenreLocalDataSource
import org.danieh.tmdb.domain.model.Genre

class RoomGenreLocalDataSource(
    private val dao: GenreDao
) : GenreLocalDataSource {

    override suspend fun insertGenres(genres: List<Genre>) =
        dao.insertGenres(genres.map { it.toDatabase() })

    override fun getAllGenres(): Flow<List<Genre>> =
        dao.getAllGenres().map { genres -> genres.map { it.toDomain() } }

    override suspend fun getGenreById(genreId: Int): Genre? =
        dao.getGenreById(genreId)?.toDomain()
}
