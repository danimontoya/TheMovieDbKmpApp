package org.danieh.tmdb

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.datetime.LocalDate
import org.danieh.tmdb.domain.model.Genre
import org.danieh.tmdb.domain.model.Movie
import org.junit.Rule
import org.junit.Test

class TheMovieAppUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val dIScope = TestDIScope.invoke(
        movies = listOf(
            Movie(
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
        )
    )

    @Test
    fun `launch app and display movies`() {
        composeTestRule.runOnIdle {
            composeTestRule.setContent {
                CompositionLocalProvider(
                    LocalLifecycleOwner provides LocalLifecycleOwnerFake(),
                ) {
                    with(dIScope) {
                        TheMovieApp(
                            popularMoviesViewModelFactory,
                            movieDetailsViewModelFactory
                        )
                    }
                }
            }
        }

        composeTestRule
            .onNodeWithText("Kraven the Hunter")
            .assertIsDisplayed()
    }
}

private class LocalLifecycleOwnerFake : LifecycleOwner {
    override val lifecycle: Lifecycle = LifecycleRegistry(this).apply {
        currentState = Lifecycle.State.RESUMED
    }
}
