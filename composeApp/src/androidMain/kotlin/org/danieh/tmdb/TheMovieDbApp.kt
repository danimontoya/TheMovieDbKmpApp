package org.danieh.tmdb

import android.app.Application
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.danieh.tmdb.data.database.TheMovieAppDatabase
import org.danieh.tmdb.data.database.datasource.RoomGenreLocalDataSource
import org.danieh.tmdb.data.database.datasource.RoomMovieGenreCrossRefLocalDataSource
import org.danieh.tmdb.data.database.datasource.RoomMovieLocalDataSource
import org.danieh.tmdb.data.database.getDatabaseBuilder
import org.danieh.tmdb.data.network.KtorNetworkService
import org.danieh.tmdb.data.network.httpClient
import org.danieh.tmdb.presentation.MovieDetailsViewModelFactory
import org.danieh.tmdb.presentation.PopularMoviesViewModelFactory
import org.danieh.tmdb.scope.AppDispatcherProvider
import org.danieh.tmdb.scope.DatabaseScope
import org.danieh.tmdb.scope.MovieDetailsViewModelFactoryScope
import org.danieh.tmdb.scope.NetworkScope
import org.danieh.tmdb.scope.PopularMoviesViewModelFactoryScope
import org.danieh.tmdb.scope.ThreadingScope


class TheMovieDbApp : Application() {

    lateinit var popularMoviesViewModelFactory: PopularMoviesViewModelFactory
    lateinit var movieDetailsViewModelFactory: MovieDetailsViewModelFactory

    private val database: TheMovieAppDatabase by lazy {
        getDatabaseBuilder(applicationContext)
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        val threadingScope = ThreadingScope.invoke(AppDispatcherProvider())
        val networkScope = NetworkScope.invoke(
            networkService = KtorNetworkService(httpClient)
        )
        val databaseScope = DatabaseScope.invoke(
            movieLocalDataSource = RoomMovieLocalDataSource(database.movieDao()),
            movieGenreCrossRefLocalDataSource = RoomMovieGenreCrossRefLocalDataSource(database.movieGenreCrossRefDao()),
            genreLocalDataSource = RoomGenreLocalDataSource(database.genreDao())
        )
        popularMoviesViewModelFactory =
            PopularMoviesViewModelFactoryScope.invoke(
                threadingScope,
                networkScope,
                databaseScope
            ).popularMoviesViewModelFactory
        movieDetailsViewModelFactory =
            MovieDetailsViewModelFactoryScope.invoke(
                threadingScope,
                networkScope,
                databaseScope
            ).movieDetailsViewModelFactory
    }
}
