package com.ecs198f.foodtrucks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabStateAdapter(fragment: Fragment, private var foodTruck: FoodTruck) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        return if(position==1) {
            FoodTruckReviewsFragment(foodTruck)
        } else {
            FoodTruckMenuFragment(foodTruck)
        }

    }
}