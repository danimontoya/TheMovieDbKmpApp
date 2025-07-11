package org.danieh.tmdb.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

object DateTimeFormatting {

    private val dateFormat = LocalDate.Format {
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        day()
    }

    internal fun LocalDate.dateString(): String = format(dateFormat)

    internal fun Int.hourMinuteString(): String {
        val hours = this / 60
        val minutes = this % 60

        return buildString {
            if (hours > 0) append("${hours}h ")
            if (minutes > 0 || hours == 0) append("${minutes}m")
        }.trim()
    }
}
