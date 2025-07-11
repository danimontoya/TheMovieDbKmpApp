package org.danieh.tmdb.scope

import org.danieh.tmdb.domain.usecase.observeMovieDetailsUseCase
import org.danieh.tmdb.domain.usecase.syncMovieDetailsUseCase
import org.danieh.tmdb.presentation.MovieDetailsViewModelFactory

interface MovieDetailsViewModelFactoryScope {
    val movieDetailsViewModelFactory: MovieDetailsViewModelFactory

    companion object {
        operator fun invoke(
            threadingScope: ThreadingScope,
            networkScope: NetworkScope,
            databaseScope: DatabaseScope
        ) = object : MovieDetailsViewModelFactoryScope {
            override val movieDetailsViewModelFactory: MovieDetailsViewModelFactory by lazy {
                with(threadingScope) {
                    with(networkScope) {
                        with(databaseScope) {
                            MovieDetailsViewModelFactory(
                                syncMovieDetailsUseCase = { syncMovieDetailsUseCase(it) },
                                observeMovieDetailsUseCase = { observeMovieDetailsUseCase(it) },
                                dispatchers = dispatchers
                            )
                        }
                    }
                }
            }
        }
    }
}
