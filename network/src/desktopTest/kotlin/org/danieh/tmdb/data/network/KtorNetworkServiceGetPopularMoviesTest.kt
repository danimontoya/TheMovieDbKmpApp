package org.danieh.tmdb.data.network

import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.test.runTest
import org.danieh.tmdb.domain.NetworkError
import org.danieh.tmdb.domain.model.anyMovie
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class KtorNetworkServiceGetPopularMoviesTest {

    @get:Rule
    val networkTestRule = KtorTestRule()

    @Test
    fun `after receiving 200 return success list of movies`() = runTest {
        val json = """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "backdrop_path": "/rDa3SfEijeRNCWtHQZCwfbGxYvR.jpg",
                        "genre_ids": [
                            28,
                            878,
                            12,
                            14,
                            53
                        ],
                        "id": 539972,
                        "original_language": "en",
                        "original_title": "Kraven the Hunter",
                        "overview": "Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.",
                        "popularity": 5481.159,
                        "poster_path": "/1GvBhRxY6MELDfxFrete6BNhBB5.jpg",
                        "release_date": "2024-12-11",
                        "title": "Kraven the Hunter",
                        "video": false,
                        "vote_average": 6.5,
                        "vote_count": 671
                    }
                ],
                "total_pages": 1,
                "total_results": 1
            }
        """.trimIndent()

        val networkService = KtorNetworkService(
            client = networkTestRule.serverWithResponse(
                responseCode = 200,
                body = json
            )
        )

        val expectedResult = listOf(
            anyMovie(id = 539972) to listOf(28, 878, 12, 14, 53)
        ).right()

        assertEquals(expectedResult, networkService.getPopularMovies())
    }

    @Test
    fun `after non 200 should return error`() = runTest {
        val networkService = KtorNetworkService(networkTestRule.serverWithResponse(401))
        assertEquals(NetworkError.UnknownError.left(), networkService.getPopularMovies())
    }

    @Test
    fun `after 200 should return success but returns error when deserialising date`() = runTest {
        val json = "BOOM!"
        val networkService = KtorNetworkService(networkTestRule.serverWithResponse(200, json))
        assertEquals(NetworkError.DeserializationError.left(), networkService.getPopularMovies())
    }
}
