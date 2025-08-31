package com.enxy.noolite.core.database

import android.content.Context
import androidx.room.Room

internal object AppDatabaseFactory {
    private const val TABLE_NAME = "app-database"

    fun create(
        context: Context
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, TABLE_NAME)
        .fallbackToDestructiveMigrationOnDowngrade(false)
        .build()
}
