package org.danieh.tmdb

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.danieh.tmdb.domain.model.anyMovie
import org.junit.Rule
import org.junit.Test
import kotlin.test.BeforeTest

class TheMovieAppUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val movie = anyMovie(id = 1)

    private val dIScope = TestDIScope.invoke(
        observeMoviesUseCaseResult = flowOf(listOf(movie)),
        observeMovieDetailsUseCaseResult = flowOf(movie)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `display popular movies and navigate to details`() {
        val lifecycleOwnerFake = LocalLifecycleOwnerFake()

        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalLifecycleOwner provides lifecycleOwnerFake,
            ) {
                with(dIScope) {
                    TheMovieApp(
                        popularMoviesViewModelFactory,
                        movieDetailsViewModelFactory
                    )
                }
            }
        }

        composeTestRule.runOnIdle {
            lifecycleOwnerFake.markResumed()
        }

        composeTestRule
            .onNodeWithText("Kraven the Hunter")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Kraven the Hunter")
            .performClick()

        composeTestRule
            .onNodeWithText("Overview")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.")
            .assertIsDisplayed()
    }
}

private class LocalLifecycleOwnerFake : LifecycleOwner {
    private val registry = LifecycleRegistry(this)

    override val lifecycle: Lifecycle get() = registry

    fun markResumed() {
        registry.currentState = Lifecycle.State.RESUMED
    }
}
