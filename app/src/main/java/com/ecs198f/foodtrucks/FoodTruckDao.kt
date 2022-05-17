package com.ecs198f.foodtrucks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodTruckDao {
    @Query("SELECT * FROM foodTruck")
    suspend fun listAllTrucks(): List<FoodTruck>

    @Insert
    suspend fun addTruck(truck : FoodTruck)

    @Insert
    suspend fun addTruck(trucks : List<FoodTruck>)

    @Delete
    suspend fun removeTruck (truck : FoodTruck)

    @Query("DELETE FROM foodTruck")
    suspend fun removeAllTruck()
}