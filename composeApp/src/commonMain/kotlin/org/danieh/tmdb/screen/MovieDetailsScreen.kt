package org.danieh.tmdb.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.datetime.LocalDate
import org.danieh.tmdb.data.network.pictureBaseUrl
import org.danieh.tmdb.domain.model.Cast
import org.danieh.tmdb.domain.model.Genre
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.presentation.MovieDetailsViewState
import org.danieh.tmdb.screen.common.BigScreenAwareContent
import org.danieh.tmdb.util.DateTimeFormatting.dateString
import org.danieh.tmdb.util.DateTimeFormatting.hourMinuteString
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    viewState: MovieDetailsViewState,
    onBackButtonClicked: () -> Unit
) = Scaffold(
    topBar = {
        TopAppBar(
            title = { Text(text = "Movie Details") },
            navigationIcon = {
                IconButton(onClick = onBackButtonClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

            }
        )
    }
) { innerPadding ->
    BigScreenAwareContent(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
        ) {
            viewState.movie?.let {
                MoviePoster(it)
                MovieTitle(it)
                MovieOverview(it)
            }
            MovieCast(viewState.cast)
        }
    }
}

@Composable
fun MoviePoster(
    movie: Movie
) = Box(modifier = Modifier.padding(horizontal = 16.dp)) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data("$pictureBaseUrl${movie.backdropPath}")
            .crossfade(true)
            .build(),
        contentDescription = "Image",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(start = 60.dp)
            .clip(shape = RoundedCornerShape(10.dp))
    )
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data("$pictureBaseUrl${movie.posterPath}")
            .crossfade(true).build(),
        contentDescription = "Image",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .width(120.dp)
            .height(160.dp)
            .align(Alignment.CenterStart)
            .clip(shape = RoundedCornerShape(10.dp))
    )
}

@Composable
fun MovieTitle(movie: Movie) {

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = buildAnnotatedString {
            append("${movie.title} ")
            withStyle(style = SpanStyle(color = Color.Gray)) { append("(${movie.releaseDate.year})") }
        },
        style = MaterialTheme.typography.titleLarge,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Release:",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            modifier = Modifier.padding(end = 10.dp)
        )

        val releaseDate = movie.releaseDate.dateString()
        val originalLanguage = "(${movie.originalLanguage.uppercase()})"
        val duration = movie.runtime?.hourMinuteString().orEmpty()
        Text(
            text = "$releaseDate $originalLanguage $duration",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            modifier = Modifier.padding(end = 10.dp)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = movie.genres.joinToString(", ") { it.name },
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgress((movie.voteAverage.toFloat().div(10)))

        Text(
            text = "User Score",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 10.dp, end = 15.dp)
        )

        HorizontalDivider(
            modifier = Modifier
                .height(20.dp)
                .width(3.dp)
                .background(Color.LightGray)
        )

        Row(
            modifier = Modifier.padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Arrow",
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Play Trailer",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(end = 10.dp)
            )

        }
    }
}

@Composable
fun MovieOverview(movie: Movie) {
    Spacer(modifier = Modifier.height(15.dp))
    Text(
        text = "Overview",
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        modifier = Modifier.padding(start = 15.dp)
    )
    Spacer(modifier = Modifier.height(10.dp))
    val lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 4 / 3
    Text(
        text = movie.overview,
        style = MaterialTheme.typography.bodyLarge,
        lineHeight = lineHeight,
        modifier = Modifier.padding(horizontal = 15.dp)
    )
}

@Composable
fun CircularProgress(progressValue: Float) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .clip(shape = CircleShape)
            .background(color = Color.Black)
    ) {
        CircularProgressIndicator(
            progress = { progressValue },
            modifier = Modifier
                .align(Alignment.Center)
                .height(38.dp)
                .width(38.dp),
            color = Color.Yellow,
            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
        )
        val percentage = (progressValue * 100).toInt()
        Text(
            text = buildAnnotatedString {
                append("$percentage")
                withStyle(style = SpanStyle(fontSize = 10.sp)) {
                    append(
                        "%"
                    )
                }
            },
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp,
            maxLines = 1,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun MovieCast(castList: List<Cast>) {
    Spacer(modifier = Modifier.height(15.dp))
    Text(
        text = "Top Billed Cast",
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        modifier = Modifier.padding(horizontal = 15.dp)
    )
    Spacer(modifier = Modifier.height(10.dp))
    LazyRow(content = {
        items(castList) {
            MovieCastCard(it)
        }
    })
    Spacer(modifier = Modifier.height(15.dp))
}

@Composable
fun MovieCastCard(cast: Cast) {
    Card(
        modifier = Modifier
            .padding(start = 15.dp)
            .background(color = Color.White)
            .clickable {

            },
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.width(120.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data("https://image.tmdb.org/t/p/w500" + cast.profilePath)
                    .crossfade(true)
                    .build(),
                contentDescription = "Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = cast.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 10.dp),
            )
            Text(
                text = cast.character,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 10.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

}

@Preview
@Composable
private fun MovieDetailsScreenPreview() = MaterialTheme {
    val viewState = MovieDetailsViewState(
        movie = Movie(
            adult = false,
            backdropPath = "/v9Du2HC3hlknAvGlWhquRbeifwW.jpg",
            id = 539972,
            originalLanguage = "en",
            originalTitle = "Kraven the Hunter",
            overview = "Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.",
            popularity = 2600.343,
            posterPath = "/i47IUSsN126K11JUzqQIOi1Mg1M.jpg",
            releaseDate = LocalDate.parse("2024-12-11"),
            runtime = 127,
            title = "Kraven the Hunter",
            video = false,
            voteAverage = 6.6,
            voteCount = 881,
            genres = listOf(Genre(1, "Drama"))
        ),
        cast = listOf(
            Cast(
                adult = false,
                gender = 2,
                id = 27428,
                knownForDepartment = "Acting",
                name = "Aaron Taylor-Johnson",
                originalName = "Aaron Taylor-Johnson",
                popularity = 40.731,
                profilePath = "/pFtHhih2XEaFaD3qOFyQW6q83br.jpg",
                castId = 8,
                character = "Sergei Kravinoff / Kraven the Hunter",
                creditId = "60aec3f9d29bdd002c022ce0",
                order = 0
            ),
            Cast(
                adult = false,
                gender = 1,
                id = 1437491,
                knownForDepartment = "Acting",
                name = "Ariana DeBose",
                originalName = "Ariana DeBose",
                popularity = 18.354,
                profilePath = "/8HTSA2iVTsDN83OncAvFTcqxsAr.jpg",
                castId = 14,
                character = "Calypso Ezili",
                creditId = "621e8519f12cf4001b7f2ccf",
                order = 1
            ),
            Cast(
                adult = false,
                gender = 2,
                id = 2099497,
                knownForDepartment = "Acting",
                name = "Fred Hechinger",
                originalName = "Fred Hechinger",
                popularity = 23.782,
                profilePath = "/99ctABEIEwNl4qjIZcLODgwnx0M.jpg",
                castId = 13,
                character = "Dmitri Kravinoff",
                creditId = "6215349844a4240046d3e286",
                order = 2
            ),
            Cast(
                adult = false,
                gender = 2,
                id = 4941,
                knownForDepartment = "Acting",
                name = "Alessandro Nivola",
                originalName = "Alessandro Nivola",
                popularity = 10.585,
                profilePath = "/53wfpjSwPTMwhfuOSdgGgojMI8m.jpg",
                castId = 15,
                character = "Aleksei Sytsevich",
                creditId = "622278d5c2823a006cde2a35",
                order = 3
            ),
            Cast(
                adult = false,
                gender = 2,
                id = 984629,
                knownForDepartment = "Acting",
                name = "Christopher Abbott",
                originalName = "Christopher Abbott",
                popularity = 28.759,
                profilePath = "/qWmlTycQb3yXaGhxPb6LCyaDjqh.jpg",
                castId = 17,
                character = "The Foreigner",
                creditId = "6228de050a517c00475e04b6",
                order = 4
            ),
            Cast(
                adult = false,
                gender = 2,
                id = 934,
                knownForDepartment = "Acting",
                name = "Russell Crowe",
                originalName = "Russell Crowe",
                popularity = 62.148,
                profilePath = "/fbzD4utSGJlsV8XbYMLoMdEZ1Fc.jpg",
                castId = 12,
                character = "Nikolai Kravinoff",
                creditId = "6204172d4df291009c195a66",
                order = 5
            ),
            Cast(
                adult = false,
                gender = 2,
                id = 238130,
                knownForDepartment = "Acting",
                name = "Yuri Kolokolnikov",
                originalName = "Юрий Колокольников",
                popularity = 8.491,
                profilePath = "/77g4exFoU4FvCWoWVgn6aXR01rA.jpg",
                castId = 163,
                character = "Semyon Chorney",
                creditId = "675f4dc0d6f5e8458b8b4a7a",
                order = 6
            ),
            Cast(
                adult = false,
                gender = 2,
                id = 1313583,
                knownForDepartment = "Acting",
                name = "Levi Miller",
                originalName = "Levi Miller",
                popularity = 16.923,
                profilePath = "/5ovVnN2ffAWzwhZQKZWZWUgf6tZ.jpg",
                castId = 18,
                character = "Young Sergei",
                creditId = "62585a9f43cd54016091536c",
                order = 7
            ),
            Cast(
                adult = false,
                gender = 2,
                id = 1546022,
                knownForDepartment = "Acting",
                name = "Tom Reed",
                originalName = "Tom Reed",
                popularity = 2.772,
                profilePath = "/gDvNQXHOQNBM5HyK9GfKu0tzPP1.jpg",
                castId = 167,
                character = "Bert",
                creditId = "675f4e99a8d0eb560faa5f78",
                order = 8
            ),
            Cast(
                adult = false,
                gender = 2,
                id = 1840063,
                knownForDepartment = "Acting",
                name = "Billy Barratt",
                originalName = "Billy Barratt",
                popularity = 4.36,
                profilePath = "/h8XlTs26su2Rg3oLtShERYEnVc6.jpg",
                castId = 158,
                character = "Young Dmitri",
                creditId = "67592d635c364c4a67ffcffd",
                order = 9
            )
        )
    )

    MovieDetailsScreen(
        viewState,
        onBackButtonClicked = { }
    )
}
