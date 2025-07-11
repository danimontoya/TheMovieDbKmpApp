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
                with(threadingScope) {
                    with(networkScope) {
                        with(databaseScope) {
                            PopularMoviesViewModelFactory(
                                syncMoviesUseCase = { syncMoviesUseCase() },
                                syncGenresUseCase = { syncGenresUseCase() },
                                observeMoviesUseCase = { observeAllMovies() },
                                dispatchers = dispatchers
                            )
                        }
                    }
                }
//                // No context argument for 'NetworkScope' found / No context argument for 'DatabaseScope' found.
//                context(threadingScope, networkScope, databaseScope) {
//                    PopularMoviesViewModelFactory(
//                        syncMoviesUseCase = { syncMoviesUseCase() },
//                        syncGenresUseCase = { syncGenresUseCase() },
//                        observeMoviesUseCase = { observeAllMovies() },
//                        dispatchers = threadingScope.dispatchers
//                    )
//                }
            }
        }
    }
}
