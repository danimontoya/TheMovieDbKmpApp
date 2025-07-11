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
import org.danieh.tmdb.domain.model.Movie
import org.danieh.tmdb.scope.DispatcherProvider

class PopularMoviesViewModel(
    private val syncMoviesUseCase: suspend () -> Either<NetworkError, Unit>,
    private val syncGenresUseCase: suspend () -> Either<NetworkError, Unit>,
    observeMoviesUseCase: () -> Flow<List<Movie>>,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val viewState = MutableStateFlow(
        PopularMoviesViewState(
            movies = emptyList(),
            isLoading = true,
            isSyncMoviesError = false,
            isSyncGenresError = false,
        )
    )

    init {
        syncGenres()
        observeMoviesUseCase()
            .flowOn(dispatchers.Default)
            .onEach { movies ->
                viewState.update {
                    it.copy(
                        movies = movies,
                        isLoading = false
                    )
                }
            }
            .catch { println("Error ${it.message}") }
            .launchIn(viewModelScope)

    }

    private fun syncGenres() = viewModelScope.launch {
        withContext(dispatchers.Default) { syncGenresUseCase() }
            .map { syncMovies() }
            .mapLeft { viewState.update { it.copy(isSyncGenresError = true) } }
    }

    private fun syncMovies() =
        viewModelScope.launch {
            withContext(dispatchers.Default) { syncMoviesUseCase() }
                .mapLeft { viewState.update { it.copy(isSyncMoviesError = true) } }
        }
}

data class PopularMoviesViewState(
    val movies: List<Movie>,
    val isLoading: Boolean,
    val isSyncMoviesError: Boolean,
    val isSyncGenresError: Boolean,
)

class PopularMoviesViewModelFactory(
    private val syncMoviesUseCase: suspend () -> Either<NetworkError, Unit>,
    private val syncGenresUseCase: suspend () -> Either<NetworkError, Unit>,
    private val observeMoviesUseCase: () -> Flow<List<Movie>>,
    private val dispatchers: DispatcherProvider
) {
    internal fun create(): ViewModelProvider.Factory =
        viewModelFactory {
            PopularMoviesViewModel(
                syncMoviesUseCase = syncMoviesUseCase,
                syncGenresUseCase = syncGenresUseCase,
                observeMoviesUseCase = observeMoviesUseCase,
                dispatchers = dispatchers
            )
        }
}
