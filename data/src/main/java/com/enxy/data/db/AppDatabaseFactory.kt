package com.enxy.data.db

import android.content.Context
import androidx.room.Room

internal object AppDatabaseFactory {
    private const val TABLE_NAME = "app-database"

    fun create(
        context: Context
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, TABLE_NAME)
        .fallbackToDestructiveMigrationOnDowngrade()
        .build()
}
