package com.ecs198f.foodtrucks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ecs198f.foodtrucks.databinding.FragmentFoodTruckDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodTruckDetailFragment : Fragment() {
    private val args: FoodTruckDetailFragmentArgs by navArgs()
    private lateinit var TabStateAdapter: TabStateAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFoodTruckDetailBinding.inflate(inflater, container, false)
        val recyclerViewAdapter = FoodItemListRecyclerViewAdapter(listOf())


        TabStateAdapter = TabStateAdapter(this,args.foodTruck)

        viewPager = binding.foodTruckDetailViewPager2
        viewPager.adapter = TabStateAdapter

        val tabLayout = binding.foodTruckDetailTabLayout

        TabLayoutMediator(tabLayout, viewPager) { _, _ ->

        }.attach()
        tabLayout.getTabAt(0)?.setText("Menu");
        tabLayout.getTabAt(1)?.setText("Reviews");

        args.foodTruck.let {
            binding.apply {
                Glide.with(root).load(it.imageUrl).into(foodTruckDetailImage)
                foodTruckDetailPriceLevel.text = "$".repeat(it.priceLevel)
                foodTruckDetailLocation.text = it.location
                foodTruckDetailTime.text = it.formattedTimeInterval
            }

            (requireActivity() as MainActivity).apply {
                title = it.name
                var itemDao = db.itemDao()
                foodTruckService.listFoodItems(it.id).enqueue(object : Callback<List<FoodItem>> {
                    override fun onResponse(
                        call: Call<List<FoodItem>>,
                        response: Response<List<FoodItem>>
                    ) {
                        var body = response.body()
                        recyclerViewAdapter.updateItems(body!!)
                        lifecycleScope.launch {
                            itemDao.removeItem(it.id)
                            itemDao.addItem(body!!)
                        }
                    }

                    override fun onFailure(call: Call<List<FoodItem>>, t: Throwable) {
                        lifecycleScope.launch {
                            recyclerViewAdapter.updateItems(itemDao.listItemsOfTruck(it.id))
                        }
                    }
                })
            }
        }

        return binding.root
    }
}