package org.danieh.tmdb.presentation

import app.cash.turbine.test
import arrow.core.right
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.danieh.tmdb.TestDispatcherProvider
import org.danieh.tmdb.domain.model.Genre
import org.danieh.tmdb.domain.model.Movie
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieDetailsViewModelTest {

    private val testDispatcherProvider = TestDispatcherProvider()

    @Test
    fun `viewModel emits updated movie on observe`() = runTest {
        val fakeMovie = Movie(
            adult = false,
            backdropPath = "/rDa3SfEijeRNCWtHQZCwfbGxYvR.jpg",
            genres = listOf(Genre(1, "Drama")),
            id = 1,
            originalLanguage = "en",
            originalTitle = "Kraven the Hunter",
            overview = "Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.",
            popularity = 5481.159,
            posterPath = "/1GvBhRxY6MELDfxFrete6BNhBB5.jpg",
            releaseDate = LocalDate.parse("2024-12-11"),
            runtime = null,
            title = "Kraven the Hunter",
            video = false,
            voteAverage = 6.5,
            voteCount = 671
        )

        val viewModel = MovieDetailsViewModel(
            movieId = 1,
            syncMovieDetailsUseCase = { Unit.right() },
            observeMovieDetailsUseCase = { flowOf(fakeMovie) },
            dispatchers = testDispatcherProvider
        )

        viewModel.viewState.test {
            assertEquals(null, awaitItem().movie)
            assertEquals(fakeMovie, awaitItem().movie)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
