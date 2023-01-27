package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The AsteroidDatabaseEntity represents an entity in the table of asteroid_database_table
 */
@Entity(tableName = "asteroid_database_table")
data class AsteroidDatabaseEntity constructor(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "codename_column")
    val codename: String,
    @ColumnInfo(name = "closeApproachDate_column")
    val closeApproachDate: String,
    @ColumnInfo(name = "absoluteMagnitude_column")
    val absoluteMagnitude: Double,
    @ColumnInfo(name = "estimatedDiameter_column")
    val estimatedDiameter: Double,
    @ColumnInfo(name = "relativeVelocity_column")
    val relativeVelocity: Double,
    @ColumnInfo(name = "distanceFromEarth_column")
    val distanceFromEarth: Double,
    @ColumnInfo(name = "isPotentiallyHazardous_column")
    val isPotentiallyHazardous: Boolean
)

