package org.danieh.tmdb

import org.danieh.tmdb.data.network.KtorNetworkService
import org.danieh.tmdb.data.network.httpClient
import org.danieh.tmdb.domain.scope.DatabaseScope
import org.danieh.tmdb.domain.scope.NetworkScope
import org.danieh.tmdb.domain.usecase.observeAllMovies
import org.danieh.tmdb.domain.usecase.observeMovieDetailsUseCase
import org.danieh.tmdb.domain.usecase.syncGenresUseCase
import org.danieh.tmdb.domain.usecase.syncMovieDetailsUseCase
import org.danieh.tmdb.domain.usecase.syncMoviesUseCase
import org.danieh.tmdb.presentation.MovieDetailsViewModelFactory
import org.danieh.tmdb.presentation.PopularMoviesViewModelFactory
import org.danieh.tmdb.scope.AppDispatcherProvider
import org.danieh.tmdb.scope.ThreadingScope

interface AppDIScope {

    val popularMoviesViewModelFactory: PopularMoviesViewModelFactory
    val movieDetailsViewModelFactory: MovieDetailsViewModelFactory

    companion object {
        operator fun invoke(
            threadingScope: ThreadingScope = ThreadingScope.invoke(AppDispatcherProvider()),
            networkScope: NetworkScope = NetworkScope.invoke(KtorNetworkService(httpClient)),
            databaseScope: DatabaseScope
        ): AppDIScope = object : AppDIScope {
            override val popularMoviesViewModelFactory: PopularMoviesViewModelFactory =
                context(threadingScope, networkScope, databaseScope) {
                    PopularMoviesViewModelFactory(
                        syncMoviesUseCase = { syncMoviesUseCase() },
                        syncGenresUseCase = { syncGenresUseCase() },
                        observeMoviesUseCase = { observeAllMovies() },
                        dispatchers = threadingScope.dispatchers
                    )
                }

            override val movieDetailsViewModelFactory: MovieDetailsViewModelFactory =
                context(threadingScope, networkScope, databaseScope) {
                    MovieDetailsViewModelFactory(
                        syncMovieDetailsUseCase = { movieId -> syncMovieDetailsUseCase(id = movieId) },
                        observeMovieDetailsUseCase = { movieId -> observeMovieDetailsUseCase(id = movieId) },
                        dispatchers = threadingScope.dispatchers
                    )
                }
        }
    }
}
