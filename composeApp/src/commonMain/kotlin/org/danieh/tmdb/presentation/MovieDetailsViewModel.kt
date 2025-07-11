package org.danieh.tmdb.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.danieh.tmdb.domain.NetworkError
import org.danieh.tmdb.domain.model.Cast
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.scope.DispatcherProvider

class MovieDetailsViewModel(
    movieId: Int,
    syncMovieDetailsUseCase: suspend (Int) -> Either<NetworkError, Unit>,
    observeMovieDetailsUseCase: (Int) -> Flow<Movie?>,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val viewState = MutableStateFlow(
        MovieDetailsViewState(movie = null, cast = emptyList())
    )

    init {
        viewModelScope.launch {
            withContext(dispatchers.Default) { syncMovieDetailsUseCase(movieId) }
                .mapLeft { println("SyncMovieDetailsUseCase - Error: $it") }
        }
        observeMovieDetailsUseCase(movieId)
            .flowOn(dispatchers.Default)
            .catch { println("Error ${it.message}") }
            .onEach { movie: Movie? ->
                viewState.update {
                    it.copy(movie = movie)
                }
            }.launchIn(viewModelScope)
    }
}

data class MovieDetailsViewState(
    val movie: Movie?,
    val cast: List<Cast>,
)

class MovieDetailsViewModelFactory(
    private val syncMovieDetailsUseCase: suspend (Int) -> Either<NetworkError, Unit>,
    private val observeMovieDetailsUseCase: (Int) -> Flow<Movie?>,
    private val dispatchers: DispatcherProvider
) {
    internal fun create(movieId: Int): ViewModelProvider.Factory =
        viewModelFactory {
            MovieDetailsViewModel(
                movieId = movieId,
                syncMovieDetailsUseCase = syncMovieDetailsUseCase,
                observeMovieDetailsUseCase = observeMovieDetailsUseCase,
                dispatchers = dispatchers
            )
        }
}
