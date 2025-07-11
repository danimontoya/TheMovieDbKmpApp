package org.danieh.tmdb.scope

import org.danieh.tmdb.domain.NetworkService

interface NetworkScope {
    val networkService: NetworkService

    companion object {
        operator fun invoke(
            networkService: NetworkService
        ): NetworkScope = object : NetworkScope {
            override val networkService: NetworkService by lazy { networkService }
        }
    }
}
