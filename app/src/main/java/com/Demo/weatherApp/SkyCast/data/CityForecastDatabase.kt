package com.Demo.weatherApp.SkyCast.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CitySearchEntity::class], version = 1)
abstract class CityForecastDatabase:RoomDatabase() {
    abstract fun cityForecastDao() : CityForecastDao

    companion object{
        @Volatile private var instance: CityForecastDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                CityForecastDatabase::class.java,
                name = "citysearch-db"
            ).build()

        fun getInstance(context: Context) : CityForecastDatabase{
            return  instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}