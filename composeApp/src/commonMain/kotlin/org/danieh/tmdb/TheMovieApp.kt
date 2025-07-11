package org.danieh.tmdb

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.danieh.tmdb.presentation.MovieDetailsViewModel
import org.danieh.tmdb.presentation.MovieDetailsViewModelFactory
import org.danieh.tmdb.presentation.PopularMoviesViewModel
import org.danieh.tmdb.presentation.PopularMoviesViewModelFactory
import org.danieh.tmdb.screen.MovieDetails
import org.danieh.tmdb.screen.MovieDetailsScreen
import org.danieh.tmdb.screen.PopularMovies
import org.danieh.tmdb.screen.PopularMoviesScreen

@Composable
fun TheMovieApp(
    popularMoviesViewModelFactory: PopularMoviesViewModelFactory,
    movieDetailsViewModelFactory: MovieDetailsViewModelFactory
) = MaterialTheme {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController: NavHostController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = PopularMovies,
        ) {
            composable<PopularMovies> {
                val viewModel = viewModel<PopularMoviesViewModel>(
                    factory = popularMoviesViewModelFactory.create()
                )
                PopularMoviesScreen(
                    viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
                    onMovieClicked = { movieId ->
                        navController.navigate(route = MovieDetails(movieId))
                    }
                )
            }
            composable<MovieDetails> {
                val viewModel = viewModel<MovieDetailsViewModel>(
                    factory = movieDetailsViewModelFactory.create(
                        movieId = it.toRoute<MovieDetails>().movieId
                    )
                )
                MovieDetailsScreen(
                    viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
                    onBackButtonClicked = navController::navigateUp
                )
            }
        }
    }
}
