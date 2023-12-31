package com.asparagas.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.asparagas.easyfood.activities.MainActivity
import com.asparagas.easyfood.activities.MealActivity
import com.asparagas.easyfood.adapters.MealsAdapter
import com.asparagas.easyfood.databinding.FragmentFavoritesBinding
import com.asparagas.easyfood.fragments.bottomsheet.MealBottomSheetFragment
import com.asparagas.easyfood.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        mealsAdapter = MealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        observeFavorites()

        setupItemTouchHelper()

        onFavoriteItemClick()

        onFavoriteItemLongClick()
    }

    private fun onFavoriteItemLongClick() {
        mealsAdapter.onLongClickListener = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal info")
        }
    }

    private fun onFavoriteItemClick() {
        mealsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealsAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity()) { meals ->
            if (meals.isEmpty()) {
                binding.imgBrokenHeart.visibility = View.VISIBLE
                binding.tvBrokenHeartTitle.visibility = View.VISIBLE
            }
            mealsAdapter.differ.submitList(meals)

        }
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = true

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    handleSwipe(viewHolder.adapterPosition)
                }
            }
        )

        itemTouchHelper.attachToRecyclerView(binding.rvFavorites)
    }

    private fun handleSwipe(position: Int) {
        val deletedMeal = mealsAdapter.differ.currentList[position]
        viewModel.deleteMeal(deletedMeal)

        Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction("Undo") {
            viewModel.insertMeal(deletedMeal)
            binding.imgBrokenHeart.visibility = View.INVISIBLE
            binding.tvBrokenHeartTitle.visibility = View.INVISIBLE
        }.show()
    }
}