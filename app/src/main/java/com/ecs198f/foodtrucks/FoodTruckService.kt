package com.ecs198f.foodtrucks

import retrofit2.Call
import retrofit2.http.*

interface FoodTruckService {
    @GET("food-trucks")
    fun listFoodTrucks(): Call<List<FoodTruck>>

    @GET("food-trucks/{id}/items")
    fun listFoodItems(@Path("id") truckId: String): Call<List<FoodItem>>

    @GET("food-trucks/{id}/reviews")
    fun listFoodTruckReviews(@Path("id") truckId: String): Call<List<FoodTruckReview>>

    @POST("food-trucks/{id}/reviews")
    fun postFoodTruckReview(@Path("id") truckId: String, @Header("Bearer") id: String, @Body post: PostReview): Call<PostReview>
}