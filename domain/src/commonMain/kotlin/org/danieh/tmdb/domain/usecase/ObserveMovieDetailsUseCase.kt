package org.danieh.tmdb.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.scope.DatabaseScope

context(databaseScope: DatabaseScope)
fun observeMovieDetailsUseCase(id: Int): Flow<Movie?> =
    databaseScope.movieLocalDataSource.getMovieById(id)
