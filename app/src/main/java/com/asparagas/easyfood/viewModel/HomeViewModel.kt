package com.asparagas.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asparagas.easyfood.pojo.Category
import com.asparagas.easyfood.pojo.CategoryList
import com.asparagas.easyfood.pojo.MealsByCategoryList
import com.asparagas.easyfood.pojo.MealsByCategory
import com.asparagas.easyfood.pojo.Meal
import com.asparagas.easyfood.pojo.MealList
import com.asparagas.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>?>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()

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

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if(response.isSuccessful){
                    response.body()?.let {categoryList ->
                        categoriesLiveData.postValue(categoryList.categories)
                    }
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                handleFailure("getCategories", t)
            }
        })
    }

    fun observeRandomMealLiveData(): LiveData<Meal> = randomMealLiveData
    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>?> = popularItemsLiveData
    fun observeCategoriesLiveData(): LiveData<List<Category>> = categoriesLiveData

    private fun handleFailure(callName: String, throwable: Throwable) {
        Log.d("HomeViewModel", "Error in $callName: ${throwable.message}")
    }
}