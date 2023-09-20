package com.asparagas.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.asparagas.easyfood.databinding.CategoriesItemsBinding
import com.asparagas.easyfood.pojo.Category
import com.bumptech.glide.Glide

class CategoriesAdapter() : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    private var categoryList = ArrayList<Category>()
    var onItemClick: ((Category) -> Unit)? = null

    inner class CategoryViewHolder(var binding: CategoriesItemsBinding) : ViewHolder(binding.root)

    fun setCategories(categoryList: ArrayList<Category>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoriesItemsBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryList[position].strCategoryThumb)
            .into(holder.binding.imgCategory)

        holder.binding.tvCategoryName.text = categoryList[position].strCategory

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(categoryList[position])
        }
    }
}