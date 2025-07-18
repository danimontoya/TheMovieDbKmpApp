package org.danieh.tmdb.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.danieh.tmdb.data.database.model.GenreEntity

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Query("SELECT * FROM genres WHERE genreId = :genreId")
    suspend fun getGenreById(genreId: Int): GenreEntity?
}
