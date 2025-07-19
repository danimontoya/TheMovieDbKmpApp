package org.danieh.tmdb.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.domain.scope.DatabaseScope

context(databaseScope: DatabaseScope)
fun observeAllMovies(): Flow<List<Movie>> =
    databaseScope.movieLocalDataSource.getAllMovies()
