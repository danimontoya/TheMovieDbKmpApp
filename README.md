## TheMovieDb KMP app playground

- This is a playground Kotlin Multiplatform project displaying information from TheMovieDb API
  targeting Android, iOS, Web, Desktop.

### Dependencies
```
TheMovieDbKmpApp Project Dependencies:
  Configuration: androidMainImplementation
- project: :database
  Configuration: commonMainImplementation
- project: :domain
- project: :network
  Configuration: iosMainImplementation
- project: :database
```

#### Modules / Stack

- This is a KMP app targeting mobile, web and desktop where they all have the same `domain` for
  business logic
- All targets make use of the `network` module (**Ktor**) for accessing the API.
- Mobile targets make use of `database` (**Room** - bundled) but desktop and web dont (in-memory) for
  storing information
- `composeApp` finally wires all modules up using **Context Parameters** and defines the UI +
  navigation
- PS: If you want to run it, you might want to create your own API-key on the movie db site and add
  within the [httpClient](org.danieh.tmdb.data.network.HttpClientKt.getHttpClient).
