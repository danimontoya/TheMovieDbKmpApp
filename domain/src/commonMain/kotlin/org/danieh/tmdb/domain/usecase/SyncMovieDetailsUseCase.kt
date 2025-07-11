package org.danieh.tmdb.domain.usecase

import arrow.core.Either
import org.danieh.tmdb.domain.NetworkError
import org.danieh.tmdb.scope.DatabaseScope
import org.danieh.tmdb.scope.NetworkScope

context(networkScope: NetworkScope, databaseScope: DatabaseScope)
suspend fun syncMovieDetailsUseCase(id: Int): Either<NetworkError, Unit> =
    networkScope.networkService.getMovieDetails(id)
        .map { movieDetails ->
            movieDetails.runtime?.let { runtime ->
                databaseScope.movieLocalDataSource.updateMovie(id, runtime)
            }
        }
