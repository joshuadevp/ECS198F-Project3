package com.ecs198f.foodtrucks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ecs198f.foodtrucks.databinding.FragmentFoodTruckReviewsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodTruckReviewsFragment(private val foodTruck: FoodTruck) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFoodTruckReviewsBinding.inflate(inflater, container, false)
        val recyclerViewAdapter = FoodTruckReviewListRecyclerViewAdapter(listOf())

        binding.apply {
            foodTruckReviewsRecyclerView.apply {
                adapter = recyclerViewAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        (requireActivity() as MainActivity).apply {
            title = foodTruck.name

            foodTruckService.listFoodTruckReviews(foodTruck.id).enqueue(object : Callback<List<FoodTruckReview>> {
                override fun onResponse(
                    call: Call<List<FoodTruckReview>>,
                    response: Response<List<FoodTruckReview>>
                ) {
                    recyclerViewAdapter.updateItems(response.body()!!)
                }

                override fun onFailure(call: Call<List<FoodTruckReview>>, t: Throwable) {
                    throw t
                }
            })
        }

        return binding.root
    }
}