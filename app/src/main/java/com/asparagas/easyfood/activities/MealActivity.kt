package com.asparagas.easyfood.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.asparagas.easyfood.R
import com.asparagas.easyfood.databinding.ActivityMealBinding
import com.asparagas.easyfood.db.MealDatabase
import com.asparagas.easyfood.fragments.HomeFragment
import com.asparagas.easyfood.pojo.Meal
import com.asparagas.easyfood.viewModel.MealViewModel
import com.asparagas.easyfood.viewModel.MealViewModelFactory
import com.bumptech.glide.Glide

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private var mealYoutubeLink: String? = null
    private lateinit var mealViewModel: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mealDatabase = MealDatabase.getInstance(this)
        val mealViewModelFactory = MealViewModelFactory(mealDatabase)
        mealViewModel = ViewModelProvider(this, mealViewModelFactory)[MealViewModel::class.java]

        getInformationFromIntent()

        setInformationInViews()

        loadingCase()
        mealViewModel.getMealDetails(mealId)

        observerMealDetailsLiveData()

        youtubeButtonClick()

        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.addToFav.setOnClickListener {
            mealToSave?.let {
                mealViewModel.insertMeal(it)
                Toast.makeText(this, "Meal Added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun youtubeButtonClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealYoutubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave: Meal? = null
    private fun observerMealDetailsLiveData() {
        mealViewModel.observeMealDetailsLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(value: Meal) {
                val meal = value
                mealToSave = meal
                Log.e("TEST", mealToSave!!.strMeal.toString())

                binding.detailCategory.text = "Category: ${meal.strMeal}"
                binding.detailArea.text = "Area: ${meal.strArea}"
                binding.istructionsSteps.text = meal.strInstructions

                mealYoutubeLink = meal.strYoutube

                onResponseCase()
            }
        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
    }

    private fun getInformationFromIntent() {
        val intent = intent

        mealId = intent.getStringExtra(HomeFragment.MEAL_ID) ?: ""
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME) ?: ""
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB) ?: ""
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.detailCategory.visibility = View.INVISIBLE
        binding.detailArea.visibility = View.INVISIBLE
        binding.addToFav.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.detailCategory.visibility = View.VISIBLE
        binding.detailArea.visibility = View.VISIBLE
        binding.addToFav.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}