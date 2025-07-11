package org.danieh.tmdb.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.danieh.tmdb.data.database.model.MovieGenreCrossRefEntity

@Dao
interface MovieGenreCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenreIds(movieGenreCrossRefEntityId: MovieGenreCrossRefEntity)
}
