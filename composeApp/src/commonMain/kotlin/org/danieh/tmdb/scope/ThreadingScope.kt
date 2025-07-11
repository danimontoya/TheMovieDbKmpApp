package org.danieh.tmdb.scope

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface ThreadingScope {

    val dispatchers: DispatcherProvider

    companion object {
        operator fun invoke(dispatchers: DispatcherProvider) =
            object : ThreadingScope {
                override val dispatchers: DispatcherProvider by lazy { dispatchers }
            }
    }
}

interface DispatcherProvider {
    val Default: CoroutineDispatcher
    val Main: CoroutineDispatcher
}

class AppDispatcherProvider : DispatcherProvider {
    override val Default: CoroutineDispatcher = Dispatchers.Default
    override val Main: CoroutineDispatcher = Dispatchers.Main
}
