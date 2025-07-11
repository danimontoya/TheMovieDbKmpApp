package org.danieh.tmdb.scope

import org.danieh.tmdb.domain.usecase.observeAllMovies
import org.danieh.tmdb.domain.usecase.syncGenresUseCase
import org.danieh.tmdb.domain.usecase.syncMoviesUseCase
import org.danieh.tmdb.presentation.PopularMoviesViewModelFactory

interface PopularMoviesViewModelFactoryScope {
    val popularMoviesViewModelFactory: PopularMoviesViewModelFactory

    companion object {
        operator fun invoke(
            threadingScope: ThreadingScope,
            networkScope: NetworkScope,
            databaseScope: DatabaseScope
        ) = object : PopularMoviesViewModelFactoryScope {
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
        }
    }
}
