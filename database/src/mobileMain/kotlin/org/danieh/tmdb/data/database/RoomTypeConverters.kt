package org.danieh.tmdb.data.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class RoomTypeConverters {

    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? {
        return value?.unsafeToLocalDate()
    }

    @TypeConverter
    fun localDateToString(value: LocalDate?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun intListToCommaDelimitedString(value: List<Int>?): String? =
        value?.joinToString(separator = ",")

    @TypeConverter
    fun commaDelimitedStringToIntList(value: String?): List<Int>? = when {
        value == null -> null
        value.isEmpty() -> emptyList()
        else -> value.split(',').map { it.toInt() }
    }
}
