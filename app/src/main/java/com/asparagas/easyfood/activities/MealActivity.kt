package com.asparagas.easyfood.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.asparagas.easyfood.R
import com.asparagas.easyfood.databinding.ActivityMealBinding
import com.asparagas.easyfood.fragments.HomeFragment
import com.asparagas.easyfood.viewModel.MealViewModel
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

        mealViewModel = ViewModelProvider(this)[MealViewModel::class.java]

        getInformationFromIntent()

        setInformationInViews()

        loadingCase()
        mealViewModel.getMealDetails(mealId)

        observerMealDetailsLiveData()

        youtubeButtonClick()
    }

    private fun youtubeButtonClick() {
        binding.imgYoutube.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(mealYoutubeLink))
            startActivity(intent)
        }
    }

    private fun observerMealDetailsLiveData() {
        mealViewModel.observeMealDetailsLiveData().observe(this) { meal ->
            meal?.let {
                binding.detailCategory.text = "Category: ${it.strMeal}"
                binding.detailArea.text = "Area: ${it.strArea}"
                binding.istructionsSteps.text = it.strInstructions

                mealYoutubeLink=it.strYoutube

                onResponseCase()
            }
        }
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

    private fun loadingCase(){
        binding.progressBar.visibility=View.VISIBLE
        binding.detailCategory.visibility=View.INVISIBLE
        binding.detailArea.visibility=View.INVISIBLE
        binding.addToFav.visibility=View.INVISIBLE
        binding.tvInstructions.visibility=View.INVISIBLE
        binding.imgYoutube.visibility=View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.progressBar.visibility=View.INVISIBLE
        binding.detailCategory.visibility=View.VISIBLE
        binding.detailArea.visibility=View.VISIBLE
        binding.addToFav.visibility=View.VISIBLE
        binding.tvInstructions.visibility=View.VISIBLE
        binding.imgYoutube.visibility=View.VISIBLE
    }
}