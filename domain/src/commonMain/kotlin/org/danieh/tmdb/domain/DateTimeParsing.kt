package org.danieh.tmdb.domain

import arrow.core.Either
import arrow.core.getOrElse
import kotlinx.datetime.LocalDate

/**
 * Create a [LocalDate] from a string containing an ISO 8601 date, such as "2017-02-23".
 * Trying to parse a string with a date and time, e.g. "2017-02-23T22:09:00.000Z", will fail.
 * @throws kotlinx.datetime.DateTimeFormatException.DateTimeFormatException if the text cannot be parsed
 */
fun String.toLocalDate(): Either<Throwable, LocalDate> =
    Either.catch { LocalDate.parse(this) }

fun String.unsafeToLocalDate(): LocalDate =
    toLocalDate().getOrElse { throw it }
