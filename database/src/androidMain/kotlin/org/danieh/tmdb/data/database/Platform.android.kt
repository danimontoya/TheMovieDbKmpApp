package org.danieh.tmdb.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<TheMovieAppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("movies_database.db")
    return Room.databaseBuilder<TheMovieAppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
