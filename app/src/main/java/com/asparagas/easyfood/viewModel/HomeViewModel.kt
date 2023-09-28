package com.asparagas.easyfood.viewModel

import android.util.Log
import androidx.core.widget.ListViewAutoScrollHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asparagas.easyfood.db.MealDatabase
import com.asparagas.easyfood.pojo.Category
import com.asparagas.easyfood.pojo.CategoryList
import com.asparagas.easyfood.pojo.MealsByCategoryList
import com.asparagas.easyfood.pojo.MealsByCategory
import com.asparagas.easyfood.pojo.Meal
import com.asparagas.easyfood.pojo.MealList
import com.asparagas.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>?>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetLiveData = MutableLiveData<Meal>()
    private var searchedMealLiveData = MutableLiveData<List<Meal>>()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    if (!meals.isNullOrEmpty()) {
                        randomMealLiveData.value = meals[0]
                    }
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                handleFailure("getRandomMeal", t)
            }
        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getMostPopularItems("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.isSuccessful) {
                        val meals = response.body()?.meals
                        popularItemsLiveData.value = meals
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    handleFailure("getPopularItems", t)
                }

            })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.isSuccessful) {
                    response.body()?.let { categoryList ->
                        categoriesLiveData.postValue(categoryList.categories)
                    }
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                handleFailure("getCategories", t)
            }
        })
    }

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val meal = response.body()?.meals?.first()
                    meal?.let {
                        bottomSheetLiveData.postValue(it)
                    }
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                handleFailure("getMealById", t)
            }

        })
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().update(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun searchedMeals(searchQuery: String) {
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList?.let {
                    searchedMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                handleFailure("searchedMeals",t)
            }

        })
    }

    fun observeRandomMealLiveData(): LiveData<Meal> = randomMealLiveData
    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>?> = popularItemsLiveData
    fun observeCategoriesLiveData(): LiveData<List<Category>> = categoriesLiveData
    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>> = favoritesMealsLiveData
    fun observeBottomSheetLiveData(): LiveData<Meal> = bottomSheetLiveData
    fun observeSearchedMealLiveData(): LiveData<List<Meal>> = searchedMealLiveData


    private fun handleFailure(callName: String, throwable: Throwable) {
        Log.d("HomeViewModel", "Error in $callName: ${throwable.message}")
    }
}