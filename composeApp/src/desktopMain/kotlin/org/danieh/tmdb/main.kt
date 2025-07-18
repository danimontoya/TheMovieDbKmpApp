package org.danieh.tmdb

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.danieh.tmdb.data.database.datasource.InMemoryGenreLocalDataSource
import org.danieh.tmdb.data.database.datasource.InMemoryMovieGenreCrossRefLocalDataSource
import org.danieh.tmdb.data.database.datasource.InMemoryMovieLocalDataSource
import org.danieh.tmdb.scope.DatabaseScope

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TheMovieDbKmpApp",
    ) {
        val genreLocalDataSource = InMemoryGenreLocalDataSource()
        val movieGenreCrossRefLocalDataSource = InMemoryMovieGenreCrossRefLocalDataSource()
        with(
            AppDIScope.invoke(
                databaseScope = DatabaseScope.invoke(
                    movieLocalDataSource = InMemoryMovieLocalDataSource(
                        genreLocalDataSource = genreLocalDataSource,
                        movieGenreCrossRefLocalDataSource = movieGenreCrossRefLocalDataSource
                    ),
                    genreLocalDataSource = genreLocalDataSource,
                    movieGenreCrossRefLocalDataSource = movieGenreCrossRefLocalDataSource
                )
            )
        ) {
            TheMovieApp(
                popularMoviesViewModelFactory,
                movieDetailsViewModelFactory
            )
        }
    }
}