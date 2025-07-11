package org.danieh.tmdb

import org.danieh.tmdb.data.database.datasource.InMemoryGenreLocalDataSource
import org.danieh.tmdb.data.database.datasource.InMemoryMovieGenreCrossRefLocalDataSource
import org.danieh.tmdb.data.database.datasource.InMemoryMovieLocalDataSource
import org.danieh.tmdb.data.network.KtorNetworkService
import org.danieh.tmdb.data.network.httpClient
import org.danieh.tmdb.domain.usecase.observeAllMovies
import org.danieh.tmdb.domain.usecase.observeMovieDetailsUseCase
import org.danieh.tmdb.domain.usecase.syncGenresUseCase
import org.danieh.tmdb.domain.usecase.syncMovieDetailsUseCase
import org.danieh.tmdb.domain.usecase.syncMoviesUseCase
import org.danieh.tmdb.presentation.MovieDetailsViewModelFactory
import org.danieh.tmdb.presentation.PopularMoviesViewModelFactory
import org.danieh.tmdb.scope.AppDispatcherProvider
import org.danieh.tmdb.scope.DatabaseScope
import org.danieh.tmdb.scope.NetworkScope
import org.danieh.tmdb.scope.ThreadingScope

interface AppDIScope {

    val popularMoviesViewModelFactory: PopularMoviesViewModelFactory
    val movieDetailsViewModelFactory: MovieDetailsViewModelFactory

    companion object {
        operator fun invoke(
            threadingScope: ThreadingScope = ThreadingScope.invoke(AppDispatcherProvider()),
            networkScope: NetworkScope = NetworkScope.invoke(KtorNetworkService(httpClient)),
            databaseScope: DatabaseScope = DatabaseScope.invoke(
                movieLocalDataSource = InMemoryMovieLocalDataSource(),
                genreLocalDataSource = InMemoryGenreLocalDataSource(),
                movieGenreCrossRefLocalDataSource = InMemoryMovieGenreCrossRefLocalDataSource()
            )
        ): AppDIScope = object : AppDIScope {
            override val popularMoviesViewModelFactory: PopularMoviesViewModelFactory by lazy {
                context(threadingScope, networkScope, databaseScope) {
                    PopularMoviesViewModelFactory(
                        syncMoviesUseCase = { syncMoviesUseCase() },
                        syncGenresUseCase = { syncGenresUseCase() },
                        observeMoviesUseCase = { observeAllMovies() },
                        dispatchers = threadingScope.dispatchers
                    )
                }
            }
            override val movieDetailsViewModelFactory: MovieDetailsViewModelFactory by lazy {
                context(threadingScope, networkScope, databaseScope) {
                    MovieDetailsViewModelFactory(
                        syncMovieDetailsUseCase = { syncMovieDetailsUseCase(it) },
                        observeMovieDetailsUseCase = { observeMovieDetailsUseCase(it) },
                        dispatchers = threadingScope.dispatchers
                    )
                }
            }
        }
    }

}