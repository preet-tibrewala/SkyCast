package com.Demo.weatherApp.SkyCast.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: CitySearchEntity)

    @Query("SELECT * FROM Cities")
    suspend fun getAllCities(): List<CitySearchEntity>

    @Query("SELECT * FROM Cities WHERE Name = :name LIMIT 1")
    suspend fun getCityByName(name: String): CitySearchEntity?

    @Query("UPDATE Cities SET lastUpdated = :timestamp WHERE Name = :name")
    suspend fun updateLastViewed(name: String, timestamp: Long)
}