package com.ecs198f.foodtrucks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.ecs198f.foodtrucks.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private lateinit var db : AppDatabase //= Room.databaseBuilder(applicationContext, AppDatabase::class.java, "truck-db").build()
    private lateinit var truckDao : FoodTruckDao
    private lateinit var itemDao : FoodItemDao

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): LocalDateTime {
                return LocalDateTime.parse(json!!.asString)
            }
        })
        .create()

    val foodTruckService: FoodTruckService =  Retrofit.Builder()
        .baseUrl("https://api.foodtruck.schedgo.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(FoodTruckService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "truck-db")
            .allowMainThreadQueries()
            .build()

//        val trucks = truckDao.listAllTrucks();
//        val items = itemDao.listAllItems();
//
//        if (trucks.isEmpty()) {
//
//        }
//
//        if (items.isEmpty()) {
//
//        }

        title = "Food Trucks"
    }
}