package org.danieh.tmdb.presentation

import app.cash.turbine.test
import arrow.core.right
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.danieh.tmdb.TestDispatcherProvider
import org.danieh.tmdb.domain.model.anyMovie
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieDetailsViewModelTest {

    private val testDispatcherProvider = TestDispatcherProvider()

    @Test
    fun `viewModel emits updated movie on observe`() = runTest {
        val movie = anyMovie(id = 1)

        val viewModel = MovieDetailsViewModel(
            movieId = 1,
            syncMovieDetailsUseCase = { Unit.right() },
            observeMovieDetailsUseCase = { flowOf(movie) },
            dispatchers = testDispatcherProvider
        )

        viewModel.viewState.test {
            assertEquals(null, awaitItem().movie)
            assertEquals(movie, awaitItem().movie)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
