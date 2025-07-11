package org.danieh.tmdb

import androidx.compose.ui.window.ComposeUIViewController
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.danieh.tmdb.data.database.TheMovieAppDatabase
import org.danieh.tmdb.data.database.datasource.RoomGenreLocalDataSource
import org.danieh.tmdb.data.database.datasource.RoomMovieGenreCrossRefLocalDataSource
import org.danieh.tmdb.data.database.datasource.RoomMovieLocalDataSource
import org.danieh.tmdb.data.database.getDatabaseBuilder
import org.danieh.tmdb.scope.DatabaseScope

fun MainViewController() = ComposeUIViewController {
    val database: TheMovieAppDatabase =
        getDatabaseBuilder()
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()

    with(
        AppDIScope.invoke(
            databaseScope = DatabaseScope.invoke(
                movieLocalDataSource = RoomMovieLocalDataSource(database.movieDao()),
                movieGenreCrossRefLocalDataSource = RoomMovieGenreCrossRefLocalDataSource(database.movieGenreCrossRefDao()),
                genreLocalDataSource = RoomGenreLocalDataSource(database.genreDao())
            )
        )
    ) {
        TheMovieApp(
            popularMoviesViewModelFactory,
            movieDetailsViewModelFactory
        )
    }
}
