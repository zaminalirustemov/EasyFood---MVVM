package com.asparagas.easyfood.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.asparagas.easyfood.R
import com.asparagas.easyfood.adapters.CategoryMealsAdapter
import com.asparagas.easyfood.databinding.ActivityCategoryMealsBinding
import com.asparagas.easyfood.fragments.HomeFragment
import com.asparagas.easyfood.pojo.MealsByCategory
import com.asparagas.easyfood.viewModel.CategoryMealsViewModel
import java.util.zip.Inflater

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCategoryMealsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.mealsLiveData.observe(this){mealsList->
            binding.tvCategoryCount.text= "${mealsList.size.toString()}"
            categoryMealsAdapter.setMealsList(mealsList as ArrayList<MealsByCategory>)
        }
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter= CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter
        }
    }
}