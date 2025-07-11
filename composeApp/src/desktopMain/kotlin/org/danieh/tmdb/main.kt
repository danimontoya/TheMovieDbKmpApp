package org.danieh.tmdb

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TheMovieDbKmpApp",
    ) {
        with(AppDIScope.invoke()) {
            TheMovieApp(
                popularMoviesViewModelFactory,
                movieDetailsViewModelFactory
            )
        }
    }
}