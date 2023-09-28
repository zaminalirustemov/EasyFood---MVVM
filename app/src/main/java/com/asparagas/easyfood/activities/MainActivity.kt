package com.asparagas.easyfood.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.asparagas.easyfood.R
import com.asparagas.easyfood.databinding.ActivityMainBinding
import com.asparagas.easyfood.db.MealDatabase
import com.asparagas.easyfood.fragments.CategoriesFragment
import com.asparagas.easyfood.fragments.FavoritesFragment
import com.asparagas.easyfood.fragments.HomeFragment
import com.asparagas.easyfood.viewModel.HomeViewModel
import com.asparagas.easyfood.viewModel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelProviderFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this,R.id.frag_host)

        NavigationUI.setupWithNavController(bottomNavigation,navController)

    }
}