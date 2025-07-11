package org.danieh.tmdb.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import kotlinx.datetime.LocalDate
import org.danieh.tmdb.data.network.pictureBaseUrl
import org.danieh.tmdb.domain.model.Genre
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.presentation.PopularMoviesViewState
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularMoviesScreen(
    viewState: PopularMoviesViewState,
    onMovieClicked: (Int) -> Unit
) = Scaffold(
    topBar = { TopAppBar(title = { Text(text = "Popular Movies") }) }
) { innerPadding ->
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        when {
            viewState.isLoading -> LoadingScreen()
            viewState.movies.isEmpty() -> Empty()
            else -> MoviesGridAdaptive(
                movies = viewState.movies,
                onMovieClick = onMovieClicked
            )
        }
    }
}

@Composable
fun MoviesGridAdaptive(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit
) = BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    val maxWidth = with(LocalDensity.current) { maxWidth }

    // Example: Assume each card is 160dp wide with 16dp padding
    val cardWidth = 128.dp
    val spacing = 8.dp
    val totalCardWidth = cardWidth + spacing

    val columns = (maxWidth / totalCardWidth).toInt().coerceAtLeast(1)

    MoviesGrid(
        movies = movies,
        onMovieClick = onMovieClick,
        columns = columns
    )
}

@Composable
fun MoviesGrid(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    columns: Int
) = LazyVerticalGrid(
    modifier = Modifier.fillMaxWidth().padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    columns = GridCells.Fixed(columns),
    content = {
        items(movies, key = { movie -> movie.id }) { movie ->
            MovieCard(
                movie = movie,
                onCardClick = { onMovieClick(movie.id) }
            )
        }
    })

@Composable
fun LoadingScreen() =
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Loading...")
        }
    }

@Composable
fun Empty() =
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "Error",
                modifier = Modifier.size(48.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "No popular movies to show",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(6.dp),
            )
        }
    }

@Composable
fun MovieCard(
    movie: Movie,
    onCardClick: () -> Unit
) = Card(
    modifier = Modifier.background(color = Color.Transparent),
    shape = RoundedCornerShape(8.dp),
    onClick = onCardClick
) {
    Column {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data("$pictureBaseUrl${movie.posterPath}")
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        val lineHeight = MaterialTheme.typography.bodyMedium.fontSize * 4 / 3
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 8.dp),
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            lineHeight = lineHeight
        )
        Text(
            text = movie.releaseDate.toString(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() = MaterialTheme {
    PopularMoviesScreen(
        viewState = PopularMoviesViewState(
            movies = emptyList(),
            isLoading = true,
            isSyncMoviesError = false,
            isSyncGenresError = false
        ),
        onMovieClicked = { }
    )
}

@Preview
@Composable
private fun EmptyScreenPreview() = MaterialTheme {
    PopularMoviesScreen(
        viewState = PopularMoviesViewState(
            movies = emptyList(),
            isLoading = false,
            isSyncMoviesError = false,
            isSyncGenresError = false
        ),
        onMovieClicked = { }
    )
}

@Preview
@Composable
private fun SuccessScreenPreview() = MaterialTheme {
    PopularMoviesScreen(
        viewState = PopularMoviesViewState(
            movies = listOf(
                Movie(
                    adult = false,
                    backdropPath = "/rDa3SfEijeRNCWtHQZCwfbGxYvR.jpg",
                    genres = listOf(Genre(1, "Drama")),
                    id = 539972,
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
                ),
                Movie(
                    adult = false,
                    backdropPath = "/rDa3SfEijeRNCWtHQZCwfbGxYvR.jpg",
                    genres = listOf(Genre(1, "Drama")),
                    id = 539973,
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
                ),
                Movie(
                    adult = false,
                    backdropPath = "/rDa3SfEijeRNCWtHQZCwfbGxYvR.jpg",
                    genres = listOf(Genre(1, "Drama")),
                    id = 539974,
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
                ),
            ),
            isLoading = false,
            isSyncMoviesError = false,
            isSyncGenresError = false
        ),
        onMovieClicked = { }
    )
}
