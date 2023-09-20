package com.asparagas.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asparagas.easyfood.pojo.MealsByCategory
import com.asparagas.easyfood.pojo.MealsByCategoryList
import com.asparagas.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel:ViewModel() {
    var mealsLiveData= MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName:String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let {mealsList->
                    mealsLiveData.postValue(mealsList.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                handleFailure("getMealsByCategory",t)
            }

        })
    }

    fun observeMealsLiveData(): LiveData<List<MealsByCategory>> = mealsLiveData

    private fun handleFailure(callName: String, throwable: Throwable) {
        Log.d("CategoryMealsViewModel", "Error in $callName: ${throwable.message}")
    }
}