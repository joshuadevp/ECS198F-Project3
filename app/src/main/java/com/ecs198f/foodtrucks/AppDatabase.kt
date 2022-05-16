package com.ecs198f.foodtrucks

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDateTime

@Database(entities = [FoodTruck::class, FoodItem::class], version = 1)
@TypeConverters(AppDatabase.LocalDataTimeTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun truckDao(): FoodTruckDao
    abstract fun itemDao(): FoodItemDao

    class LocalDataTimeTypeConverters {
        @TypeConverter
        fun fromString(s: String): LocalDateTime = LocalDateTime.parse(s)

        @TypeConverter
        fun toString(ldt: LocalDateTime): String = ldt.toString()
    }
}