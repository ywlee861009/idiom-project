package com.kero.idiom.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getAndroidDatabaseBuilder(ctx: Context): RoomDatabase.Builder<IdiomDatabase> {
    val dbFile = ctx.getDatabasePath("idiom.db")
    return Room.databaseBuilder<IdiomDatabase>(
        context = ctx,
        name = dbFile.absolutePath
    )
}
