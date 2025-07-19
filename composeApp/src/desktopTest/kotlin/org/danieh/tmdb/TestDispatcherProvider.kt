package org.danieh.tmdb

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.danieh.tmdb.scope.DispatcherProvider

class TestDispatcherProvider(
    dispatcher: CoroutineDispatcher = Dispatchers.Unconfined
) : DispatcherProvider {
    override val Default: CoroutineDispatcher = dispatcher
    override val Main: CoroutineDispatcher = dispatcher
}
