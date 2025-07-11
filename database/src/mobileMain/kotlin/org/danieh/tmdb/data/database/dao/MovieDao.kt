package org.danieh.tmdb.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import org.danieh.tmdb.data.database.model.MovieEntity
import org.danieh.tmdb.data.database.model.MovieWithGenres
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Transaction
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieWithGenres>>

    @Query("UPDATE movies SET runtime = :runtime WHERE movieId = :id")
    suspend fun updateMovie(id: Int, runtime: Int)

    @Transaction
    @Query("SELECT * FROM movies WHERE movieId = :movieId")
    fun getMovieWithGenres(movieId: Int): Flow<MovieWithGenres?>
}
