package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room Database for the asteroid_database_table
 */
@Database(entities = [AsteroidDatabaseEntity::class], version = 1)
abstract class AsteroidsRoomDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsRoomDatabase

fun getDatabase(context: Context): AsteroidsRoomDatabase {
    synchronized(AsteroidsRoomDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsRoomDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}

