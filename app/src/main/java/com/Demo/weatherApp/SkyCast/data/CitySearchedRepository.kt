package com.Demo.weatherApp.SkyCast.data

class CitySearchedRepository(
    private val dao: CityForecastDao
) {

    suspend fun insertSearchedCity(city: CitySearchEntity) {
        val existingCity = dao.getCityByName(city.name)
        if (existingCity == null) {
            dao.insert(city)
        }
    }

    suspend fun getAllCities(): List<CitySearchEntity> {
        return dao.getAllCities()
    }

    suspend fun updateLastViewed(cityName: String) {
        dao.updateLastViewed(cityName, System.currentTimeMillis())
    }
}