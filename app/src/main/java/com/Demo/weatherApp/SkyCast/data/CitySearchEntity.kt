package com.Demo.weatherApp.SkyCast.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cities")
data class CitySearchEntity(
    @PrimaryKey @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "LastUpdated") val lastUpdated: Long
)
