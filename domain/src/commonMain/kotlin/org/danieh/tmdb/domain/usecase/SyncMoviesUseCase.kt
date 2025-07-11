package org.danieh.tmdb.domain.usecase

import arrow.core.Either
import org.danieh.tmdb.domain.NetworkError
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.scope.DatabaseScope
import org.danieh.tmdb.scope.NetworkScope

context(networkScope: NetworkScope, databaseScope: DatabaseScope)
suspend fun syncMoviesUseCase(): Either<NetworkError, Unit> =
    networkScope.networkService.getPopularMovies()
        .map { movieList: List<Pair<Movie, List<Int>>> ->
            databaseScope.movieLocalDataSource.insertMovies(movieList.map { it.first })
            movieList.map { (movie, genreIds) ->
                genreIds.map { genreId ->
                    databaseScope.movieGenreCrossRefLocalDataSource.insertMovieGenreIds(
                        movieId = movie.id,
                        genreId = genreId
                    )
                }
            }
        }
