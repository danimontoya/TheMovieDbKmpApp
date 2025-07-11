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
import org.danieh.tmdb.data.network.KtorNetworkService
import org.danieh.tmdb.data.network.httpClient
import org.danieh.tmdb.scope.AppDispatcherProvider
import org.danieh.tmdb.scope.DatabaseScope
import org.danieh.tmdb.scope.MovieDetailsViewModelFactoryScope
import org.danieh.tmdb.scope.NetworkScope
import org.danieh.tmdb.scope.PopularMoviesViewModelFactoryScope
import org.danieh.tmdb.scope.ThreadingScope

fun MainViewController() = ComposeUIViewController {
    val threadingScope = ThreadingScope.invoke(AppDispatcherProvider())
    val networkScope = NetworkScope.invoke(
        networkService = KtorNetworkService(httpClient)
    )
    val database: TheMovieAppDatabase =
        getDatabaseBuilder()
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()

    val databaseScope = DatabaseScope.invoke(
        movieLocalDataSource = RoomMovieLocalDataSource(database.movieDao()),
        movieGenreCrossRefLocalDataSource = RoomMovieGenreCrossRefLocalDataSource(database.movieGenreCrossRefDao()),
        genreLocalDataSource = RoomGenreLocalDataSource(database.genreDao())
    )
    val popularMoviesViewModelFactory =
        PopularMoviesViewModelFactoryScope.invoke(threadingScope, networkScope, databaseScope)
            .popularMoviesViewModelFactory
    val movieDetailsViewModelFactory =
        MovieDetailsViewModelFactoryScope.invoke(threadingScope, networkScope, databaseScope)
            .movieDetailsViewModelFactory
    TheMovieApp(
        popularMoviesViewModelFactory,
        movieDetailsViewModelFactory
    )
}