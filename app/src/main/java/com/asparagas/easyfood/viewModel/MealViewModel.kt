package com.asparagas.easyfood.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asparagas.easyfood.db.MealDatabase
import com.asparagas.easyfood.pojo.Meal
import com.asparagas.easyfood.pojo.MealList
import com.asparagas.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDatabase: MealDatabase) : ViewModel() {

    private var mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetails(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    if (!meals.isNullOrEmpty()) {
                        mealDetailsLiveData.value = meals[0]
                    }
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealViewModel", t.message.toString())
            }

        })
    }

    fun observeMealDetailsLiveData(): LiveData<Meal> {
        return mealDetailsLiveData
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().update(meal)
        }
    }

}