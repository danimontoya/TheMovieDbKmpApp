package org.danieh.tmdb.domain.usecase

import arrow.core.Either
import org.danieh.tmdb.domain.NetworkError
import org.danieh.tmdb.scope.DatabaseScope
import org.danieh.tmdb.scope.NetworkScope

context(networkScope: NetworkScope, databaseScope: DatabaseScope)
suspend fun syncGenresUseCase(): Either<NetworkError, Unit> =
    networkScope.networkService.getGenres()
        .map { genres ->
            databaseScope.genreLocalDataSource.insertGenres(genres)
        }
