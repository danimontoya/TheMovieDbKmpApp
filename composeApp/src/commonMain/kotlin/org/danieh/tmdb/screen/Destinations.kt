package org.danieh.tmdb.screen

import kotlinx.serialization.Serializable

@Serializable
object PopularMovies

@Serializable
data class MovieDetails(
    val movieId: Int
)
