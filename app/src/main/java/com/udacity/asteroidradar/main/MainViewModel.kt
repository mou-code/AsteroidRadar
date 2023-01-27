package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.repository.AsteroidsFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.io.IOException


class MainViewModel(private val asteroidsRepository: AsteroidsRepository) : ViewModel() {

    /**
     * Status of the asteroids list & the image view
     */
    enum class Status { LOADING, ERROR, DONE }

    private val _asteroidsStatus = MutableLiveData<Status>()
    val asteroidsStatus: LiveData<Status>
        get() = _asteroidsStatus
    private val _imageStatus = MutableLiveData<Status>()
    val imageStatus: LiveData<Status>
        get() = _imageStatus

    /**
     * The filter of the asteroids list
     */
    val _asteroidsFilter = MutableLiveData(AsteroidsFilter.SAVED_ASTEROIDS)
    fun onChangeFilter(asteroidsFilter: AsteroidsFilter) {
        _asteroidsFilter.postValue(asteroidsFilter)
    }

    /**
     * Getting information from the Repository
     */
    val asteroids = Transformations.switchMap(_asteroidsFilter) {
        when (it!!) {
            AsteroidsFilter.WEEK_ASTEROIDS -> asteroidsRepository.weekAsteroids
            AsteroidsFilter.TODAYS_ASTEROIDS -> asteroidsRepository.todaysAsteroids
            else -> asteroidsRepository.asteroids
        }
    }
    val picoftheday = asteroidsRepository.picoftheday


    /**
     * Refreshing the data from the repository. Usibg a coroutine launch to run
     * in a background thread.
     */
    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {

        viewModelScope.launch {
            _asteroidsStatus.value = Status.DONE
            _imageStatus.value = Status.LOADING
            try {
                asteroidsRepository.refreshAsteroids()
                _asteroidsStatus.value = Status.DONE
            } catch (networkError: IOException) {
                if (asteroids.value.isNullOrEmpty())
                    _asteroidsStatus.value = Status.ERROR
            }

            try {
                asteroidsRepository.getPictureOfDay()
                _imageStatus.value = Status.DONE
            } catch (networkError: IOException) {
                if (picoftheday.value == null)
                    _imageStatus.value = Status.ERROR
            }
        }
    }


    /**
     * Factory for constructing MainViewModel with parameter
     */

    class Factory(private val repository: AsteroidsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
