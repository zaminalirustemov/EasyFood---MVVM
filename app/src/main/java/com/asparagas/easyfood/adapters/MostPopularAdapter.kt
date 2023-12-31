package com.asparagas.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.asparagas.easyfood.databinding.PopularItemsBinding
import com.asparagas.easyfood.pojo.MealsByCategory
import com.bumptech.glide.Glide

class MostPopularAdapter : RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    private var mealList = ArrayList<MealsByCategory>()
    lateinit var onItemClick: ((MealsByCategory) -> Unit)
    var onLongClickListener: ((MealsByCategory) -> Unit)? = null

    inner class PopularMealViewHolder(val binding: PopularItemsBinding) : ViewHolder(binding.root)

    fun setMeals(mealList: ArrayList<MealsByCategory>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PopularItemsBinding.inflate(inflater, parent, false)
        return PopularMealViewHolder(binding)
    }

    override fun getItemCount(): Int = mealList.size

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongClickListener?.invoke(mealList[position])
            true
        }
    }
}