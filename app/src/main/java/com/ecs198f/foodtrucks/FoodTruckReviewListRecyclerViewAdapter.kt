package com.ecs198f.foodtrucks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecs198f.foodtrucks.databinding.FoodTruckReviewBinding

class FoodTruckReviewListRecyclerViewAdapter(private var items: List<FoodTruckReview>):
    RecyclerView.Adapter<FoodTruckReviewListRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: FoodTruckReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FoodTruckReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].let {
            holder.binding.apply {
                foodTruckReviewName.text = it.authorName
                foodTruckReviewComment.text = it.content
                Glide.with(root).load(it.imageUrls).into(foodTruckReviewImage)
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<FoodTruckReview>) {
        this.items = items
        notifyDataSetChanged()
    }
}