package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidsRoomDatabase
import com.udacity.asteroidradar.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * enum for the Asteroids Filter
 */
enum class AsteroidsFilter(val value: String) {
    SAVED_ASTEROIDS("saved_asteroids"),
    TODAYS_ASTEROIDS("todays_asteroids"),
    WEEK_ASTEROIDS("week_asteroids")
}

/**
 * The Main Repository of the app (interacts with the database and retrofit api services
 */
class AsteroidsRepository(private val database: AsteroidsRoomDatabase) {

    /**
        Getting the data from the database and converting it to ta damain model
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }
    val weekAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getWeekAsteroids(todayFormattedDate())) {
            it.asDomainModel()
        }
    val todaysAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getTodaysAsteroids(todayFormattedDate())) {
            it.asDomainModel()
        }
    private val _picoftheday = MutableLiveData<PictureOfDay>()
    val picoftheday: LiveData<PictureOfDay>
        get() = _picoftheday


    /**
    Refreshing the asteroids (done once a day & everytime the app runs)
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {

            val asteroids =
                parseAsteroidsJsonResult(JSONObject(AsteroidsApi.retrofitService.getAsteroids()))
            database.asteroidDao.insertAll(asteroids.asDatabaseModel())
            database.asteroidDao.DeleteOldAsteroids(todayFormattedDate())
        }
    }

    /**
    Refreshing the asteroids (done only once a day)
     */
    suspend fun DeleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.DeleteOldAsteroids(todayFormattedDate())

        }
    }
    suspend fun getPictureOfDay() {
        // Dispatchers is a main thread because we change the value of the livedata
        withContext(Dispatchers.Main) {
            _picoftheday.value = AsteroidsApi.retrofitService.getPicOfTheDay()
        }
    }


}

