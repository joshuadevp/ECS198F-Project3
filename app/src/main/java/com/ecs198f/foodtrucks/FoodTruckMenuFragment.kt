package com.ecs198f.foodtrucks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ecs198f.foodtrucks.databinding.FragmentFoodTruckMenuBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodTruckMenuFragment(private val foodTruck: FoodTruck) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFoodTruckMenuBinding.inflate(inflater, container, false)
        val recyclerViewAdapter = FoodItemListRecyclerViewAdapter(listOf())

        binding.apply {
            foodItemListRecyclerView.apply {
                adapter = recyclerViewAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        (requireActivity() as MainActivity).apply {
            title = foodTruck.name
            var itemDao = db.itemDao()
            foodTruckService.listFoodItems(foodTruck.id).enqueue(object : Callback<List<FoodItem>> {
                override fun onResponse(
                    call: Call<List<FoodItem>>,
                    response: Response<List<FoodItem>>
                ) {
                    val body = response.body()
                    recyclerViewAdapter.updateItems(body!!)
                    lifecycleScope.launch {
                        itemDao.removeItem(foodTruck.id)
                        itemDao.addItem(body!!)
                    }
                }

                override fun onFailure(call: Call<List<FoodItem>>, t: Throwable) {
                    lifecycleScope.launch {
                        recyclerViewAdapter.updateItems(itemDao.listItemsOfTruck(foodTruck.id))
                    }
                }
            })
        }

        return binding.root
    }
}