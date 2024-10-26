package com.Demo.weatherApp.SkyCast.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.Demo.weatherApp.SkyCast.data.CityForecastDatabase
import com.Demo.weatherApp.SkyCast.data.CitySearchEntity
import com.Demo.weatherApp.SkyCast.data.CitySearchedRepository
import kotlinx.coroutines.launch

class CitySearchedViewModel(application: Application):AndroidViewModel(application){
    private val repository = CitySearchedRepository(
        CityForecastDatabase.getInstance(application).cityForecastDao()
    )

    private val _cities = MutableLiveData<List<CitySearchEntity>>()
    val cities: LiveData<List<CitySearchEntity>> = _cities

    init {
        // Initialize the list of cities asynchronously
        viewModelScope.launch {
            _cities.value = repository.getAllCities()
        }
    }

    fun addSearchedCity(cityName: String) {
        viewModelScope.launch {
            val city = CitySearchEntity(cityName, System.currentTimeMillis())
            repository.insertSearchedCity(city)
            _cities.value = repository.getAllCities()
        }
    }

    fun updateLastViewed(cityName: String) {
        viewModelScope.launch {
            repository.updateLastViewed(cityName)
        }
    }
}