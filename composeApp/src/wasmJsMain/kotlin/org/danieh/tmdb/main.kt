package org.danieh.tmdb

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.danieh.tmdb.domain.datasource.InMemoryGenreLocalDataSource
import org.danieh.tmdb.domain.datasource.InMemoryMovieGenreCrossRefLocalDataSource
import org.danieh.tmdb.domain.datasource.InMemoryMovieLocalDataSource
import org.danieh.tmdb.domain.scope.DatabaseScope

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
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