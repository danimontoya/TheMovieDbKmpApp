package org.danieh.tmdb

import android.app.Application
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.danieh.tmdb.data.database.TheMovieAppDatabase
import org.danieh.tmdb.data.database.datasource.RoomGenreLocalDataSource
import org.danieh.tmdb.data.database.datasource.RoomMovieGenreCrossRefLocalDataSource
import org.danieh.tmdb.data.database.datasource.RoomMovieLocalDataSource
import org.danieh.tmdb.data.database.getDatabaseBuilder
import org.danieh.tmdb.scope.DatabaseScope


class TheMovieDbApp : Application() {

    lateinit var appScope: AppDIScope

    private val database: TheMovieAppDatabase by lazy {
        getDatabaseBuilder(applicationContext)
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appScope = AppDIScope.invoke(
            databaseScope = DatabaseScope.invoke(
                movieLocalDataSource = RoomMovieLocalDataSource(database.movieDao()),
                movieGenreCrossRefLocalDataSource = RoomMovieGenreCrossRefLocalDataSource(database.movieGenreCrossRefDao()),
                genreLocalDataSource = RoomGenreLocalDataSource(database.genreDao())
            )
        )
    }
}
