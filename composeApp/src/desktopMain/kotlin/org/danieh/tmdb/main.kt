package org.danieh.tmdb

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.danieh.tmdb.data.database.datasource.InMemoryGenreLocalDataSource
import org.danieh.tmdb.data.database.datasource.InMemoryMovieGenreCrossRefLocalDataSource
import org.danieh.tmdb.data.database.datasource.InMemoryMovieLocalDataSource
import org.danieh.tmdb.data.network.KtorNetworkService
import org.danieh.tmdb.scope.DatabaseScope
import org.danieh.tmdb.scope.NetworkScope
import org.danieh.tmdb.scope.PopularMoviesViewModelFactoryScope
import org.danieh.tmdb.data.network.httpClient
import org.danieh.tmdb.scope.AppDispatcherProvider
import org.danieh.tmdb.scope.MovieDetailsViewModelFactoryScope
import org.danieh.tmdb.scope.ThreadingScope

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TheMovieDbKmpApp",
    ) {
        val threadingScope = ThreadingScope.invoke(AppDispatcherProvider())
        val networkScope = NetworkScope.invoke(
            networkService = KtorNetworkService(httpClient)
        )
        val databaseScope = DatabaseScope.invoke(
            movieLocalDataSource = InMemoryMovieLocalDataSource(),
            genreLocalDataSource = InMemoryGenreLocalDataSource(),
            movieGenreCrossRefLocalDataSource = InMemoryMovieGenreCrossRefLocalDataSource()
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
}