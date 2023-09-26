package com.asparagas.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.asparagas.easyfood.activities.CategoryMealsActivity
import com.asparagas.easyfood.activities.MainActivity
import com.asparagas.easyfood.activities.MealActivity
import com.asparagas.easyfood.adapters.CategoriesAdapter
import com.asparagas.easyfood.adapters.MostPopularAdapter
import com.asparagas.easyfood.databinding.FragmentHomeBinding
import com.asparagas.easyfood.pojo.Category
import com.asparagas.easyfood.pojo.MealsByCategory
import com.asparagas.easyfood.pojo.Meal
import com.asparagas.easyfood.viewModel.HomeViewModel
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.asparagas.easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.asparagas.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.asparagas.easyfood.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.asparagas.easyfood.fragments.nameCategory"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerViews()
        prepareCategoriesRecyclerViews()


        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observerPopularItems()
        onPopularItemClick()

        viewModel.getCategories()
        observerCategories()
        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerViews() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun prepareCategoriesRecyclerViews() {
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }


    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, Observer { meal ->
            meal?.let {
                Glide.with(this@HomeFragment)
                    .load(it.strMealThumb)
                    .into(binding.imgRandomMeal)

                this.randomMeal = it
            }
        })
    }

    private fun observerPopularItems() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner) { mealList ->
            mealList?.let {
                popularItemsAdapter.setMeals(it as ArrayList<MealsByCategory>)
            }
        }
    }

    private fun observerCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categories?.let {
                categoriesAdapter.setCategories(it as ArrayList<Category>)
            }
        }
    }
}