package org.danieh.tmdb.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.domain.usecase.syncMoviesUseCase
import org.danieh.tmdb.scope.NetworkScope
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SyncMoviesUseCaseTest {

    private val databaseScopeFake = DatabaseScopeFake()

    @Test
    fun `sync movies success`() = runTest {
        val networkScopeFake = NetworkScope.invoke(
            networkService = object : NetworkServiceFake() {
                override suspend fun getPopularMovies(): Either<NetworkError, List<Pair<Movie, List<Int>>>> =
                    listOf(
                        anyMovie(id = 1) to emptyList<Int>(),
                        anyMovie(id = 2) to emptyList<Int>(),
                        anyMovie(id = 3) to emptyList<Int>(),
                    ).right()
            }
        )

        val result = context(a = networkScopeFake, b = databaseScopeFake) { syncMoviesUseCase() }

        assertEquals(Unit.right(), result)

        val movies = databaseScopeFake.movieLocalDataSource.getAllMovies().first()
        assertEquals(3, movies.size)
    }

    @Test
    fun `sync movies failure`() = runTest {
        val networkScopeFake = NetworkScope.invoke(
            networkService = object : NetworkServiceFake() {
                override suspend fun getPopularMovies(): Either<NetworkError, List<Pair<Movie, List<Int>>>> =
                    NetworkError.UnknownError.left()
            }
        )

        val result = context(networkScopeFake, databaseScopeFake) { syncMoviesUseCase() }

        assertEquals(NetworkError.UnknownError.left(), result)

        val movies = databaseScopeFake.movieLocalDataSource.getAllMovies().first()
        assertEquals(0, movies.size)
    }

    @AfterTest
    fun after() = runTest { databaseScopeFake.cleanUpMovies() }
}
