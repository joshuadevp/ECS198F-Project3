package com.ecs198f.foodtrucks

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodTruckService {
    @GET("food-trucks")
    fun listFoodTrucks(): Call<List<FoodTruck>>

    @GET("food-trucks/{id}/items")
    fun listFoodItems(@Path("id") truckId: String): Call<List<FoodItem>>
}