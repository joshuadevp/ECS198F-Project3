package com.ecs198f.foodtrucks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodItemDao {
    @Query("SELECT * FROM foodItem")
    suspend fun listAllItems(): List<FoodItem>

    @Query("SELECT * FROM foodItem WHERE truckId=:truckId")
    suspend fun listItemsOfTruck(truckId: String): List<FoodItem>

    @Insert
    suspend fun addItem(item : FoodItem)

    @Insert
    suspend fun addItem(items : List<FoodItem>)

    @Delete
    suspend fun removeItem(item : FoodItem)

    @Query("DELETE FROM foodItem WHERE truckId=:truckId")
    suspend fun removeItem(truckId : String)

}