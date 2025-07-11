package org.danieh.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    private val appScope by lazy { (application as TheMovieDbApp).appScope }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            with(appScope) {
                TheMovieApp(
                    popularMoviesViewModelFactory,
                    movieDetailsViewModelFactory
                )
            }
        }
    }
}
