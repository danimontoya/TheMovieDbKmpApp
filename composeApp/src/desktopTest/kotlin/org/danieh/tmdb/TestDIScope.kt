package org.danieh.tmdb

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.danieh.tmdb.domain.NetworkError
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
            syncMoviesUseCaseResult: Either<NetworkError, Unit> = Unit.right(),
            syncGenresUseCaseResult: Either<NetworkError, Unit> = Unit.right(),
            observeMoviesUseCaseResult: Flow<List<Movie>> = flowOf(emptyList()),
            syncMovieDetailsUseCaseResult: Either<NetworkError, Unit> = Unit.right(),
            observeMovieDetailsUseCaseResult: Flow<Movie?> = flowOf(null),
        ): AppDIScope = object : AppDIScope {
            override val popularMoviesViewModelFactory: PopularMoviesViewModelFactory =
                PopularMoviesViewModelFactory(
                    syncMoviesUseCase = { syncMoviesUseCaseResult },
                    syncGenresUseCase = { syncGenresUseCaseResult },
                    observeMoviesUseCase = { observeMoviesUseCaseResult },
                    dispatchers = threadingScope.dispatchers
                )

            override val movieDetailsViewModelFactory: MovieDetailsViewModelFactory =
                MovieDetailsViewModelFactory(
                    syncMovieDetailsUseCase = { syncMovieDetailsUseCaseResult },
                    observeMovieDetailsUseCase = { observeMovieDetailsUseCaseResult },
                    dispatchers = threadingScope.dispatchers
                )
        }
    }
}
