package com.asparagas.easyfood.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asparagas.easyfood.activities.MainActivity
import com.asparagas.easyfood.activities.MealActivity
import com.asparagas.easyfood.databinding.FragmentMealBottomSheetBinding
import com.asparagas.easyfood.fragments.HomeFragment
import com.asparagas.easyfood.viewModel.HomeViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "MEAL_ID"

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMealBottomSheetBinding
    private var mealId: String? = null
    private var mealName: String? = null
    private var mealThumb: String? = null
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let { viewModel.getMealById(it) }

        observeBottomSheetMeal()

        onBottomSheetDialogClick()
    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
            if (mealName != null && mealThumb != null){
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID,mealId)
                intent.putExtra(HomeFragment.MEAL_NAME,mealName)
                intent.putExtra(HomeFragment.MEAL_THUMB,mealThumb)
                startActivity(intent)
            }
        }
    }

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetLiveData().observe(viewLifecycleOwner){meal->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)
            binding.tvBottomSheetArea.text=meal.strArea
            binding.tvBottomSheetCategory.text=meal.strCategory
            binding.bottomSheetMealName.text=meal.strMeal

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}