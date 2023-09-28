package com.asparagas.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.asparagas.easyfood.databinding.MealItemBinding
import com.asparagas.easyfood.pojo.Meal
import com.asparagas.easyfood.pojo.MealsByCategory
import com.bumptech.glide.Glide

class MealsAdapter :
    RecyclerView.Adapter<MealsAdapter.FavoritesMealsAdapterViewHolder>() {

    lateinit var onItemClick: ((Meal) -> Unit)
    var onLongClickListener: ((Meal) -> Unit)? = null

    inner class FavoritesMealsAdapterViewHolder(val binding: MealItemBinding) :
        ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean = oldItem.idMeal == newItem.idMeal

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean = oldItem == newItem
    }

    val differ=AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesMealsAdapterViewHolder {
        return FavoritesMealsAdapterViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: FavoritesMealsAdapterViewHolder, position: Int) {
        val meal= differ.currentList[position]
        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.imgMeal)

        holder.binding.tvMealName.text= meal.strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(meal)
        }

        holder.itemView.setOnLongClickListener {
            onLongClickListener?.invoke(meal)
            true
        }
    }


}