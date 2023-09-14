package com.asparagas.easyfood.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.asparagas.easyfood.R
import com.asparagas.easyfood.databinding.ActivityMainBinding
import com.asparagas.easyfood.fragments.CategoriesFragment
import com.asparagas.easyfood.fragments.FavoritesFragment
import com.asparagas.easyfood.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loadInitialFragment()
        setupBottomNavigation()
    }

    private fun loadInitialFragment() {
        loadFragment(HomeFragment())
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> loadFragment(HomeFragment())
                R.id.favoritesFragment -> loadFragment(FavoritesFragment())
                R.id.categoriesFragment -> loadFragment(CategoriesFragment())
                else -> loadFragment(HomeFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frag_host, fragment)
        transaction.commit()
    }
}