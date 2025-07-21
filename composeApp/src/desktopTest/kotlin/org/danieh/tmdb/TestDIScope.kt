package org.danieh.tmdb

import arrow.core.right
import kotlinx.coroutines.flow.flowOf
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.presentation.MovieDetailsViewModelFactory
import org.danieh.tmdb.presentation.PopularMoviesViewModelFactory
import org.danieh.tmdb.scope.ThreadingScope

interface TestDIScope : AppDIScope {

    override val popularMoviesViewModelFactory: PopularMoviesViewModelFactory
    override val movieDetailsViewModelFactory: MovieDetailsViewModelFactory

    companion object {
        operator fun invoke(
            threadingScope: ThreadingScope = ThreadingScope.invoke(TestDispatcherProvider()),
            movies: List<Movie>
        ): AppDIScope = object : AppDIScope {
            override val popularMoviesViewModelFactory: PopularMoviesViewModelFactory by lazy {
                PopularMoviesViewModelFactory(
                    syncMoviesUseCase = { Unit.right() },
                    syncGenresUseCase = { Unit.right() },
                    observeMoviesUseCase = { flowOf(movies) },
                    dispatchers = threadingScope.dispatchers
                )
            }
            override val movieDetailsViewModelFactory: MovieDetailsViewModelFactory by lazy {
                MovieDetailsViewModelFactory(
                    syncMovieDetailsUseCase = { Unit.right() },
                    observeMovieDetailsUseCase = { flowOf(movies.firstOrNull()) },
                    dispatchers = threadingScope.dispatchers
                )
            }
        }
    }
}
