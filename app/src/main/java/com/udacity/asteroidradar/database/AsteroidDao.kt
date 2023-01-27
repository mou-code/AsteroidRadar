package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * The Data Access Object: Does all the operations to the database
 */
@Dao
interface AsteroidDao {

    /**
     * Getting All Asteroids from the asteroid_database_table in DESC order (by date)
     */
    @Query("SELECT * FROM asteroid_database_table ORDER BY closeApproachDate_column DESC")
    fun getAsteroids(): LiveData<List<AsteroidDatabaseEntity>>

    /**
     * Getting All Asteroids from the asteroid_database_table in DESC order (by date)
     */
    @Query("SELECT * FROM asteroid_database_table WHERE closeApproachDate_column >= :startdate ORDER BY closeApproachDate_column DESC")
    fun getWeekAsteroids(startdate: String): LiveData<List<AsteroidDatabaseEntity>>

    /**
     * Inserting All Asteroids in the asteroid_database_table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<AsteroidDatabaseEntity>)

    /**
     * Deleting all the old asteroids (before today) in the asteroid_database_table
     */
    @Query(" DELETE FROM asteroid_database_table WHERE closeApproachDate_column < :currentDayFormatted ")
    fun DeleteOldAsteroids(currentDayFormatted: String)

    /**
     * Selecting the asteroids of today
     */
    @Query("SELECT * FROM asteroid_database_table WHERE closeApproachDate_column == :currentDayFormatted")
    fun getTodaysAsteroids(currentDayFormatted: String): LiveData<List<AsteroidDatabaseEntity>>

}
