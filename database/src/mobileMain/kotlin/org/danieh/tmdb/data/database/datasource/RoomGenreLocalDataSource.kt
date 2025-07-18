package org.danieh.tmdb.data.database.datasource

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

    override suspend fun getGenreById(genreId: Int): Genre? =
        dao.getGenreById(genreId)?.toDomain()
}
