package com.dongsu.timely.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dongsu.timely.R
import com.dongsu.timely.databinding.ActivityTimelyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimelyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimelyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimelyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
    }
    private fun setupBottomNavigation() {
        val navController = (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        binding.bottomNavigation.setupWithNavController(navController)

        setBottomNavigationVisibility(navController)
    }
    private fun setBottomNavigationVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.visibility = when (destination.id) {
                R.id.calendarFragment, R.id.groupListFragment, R.id.profileFragment, R.id.groupDateFragment, R.id.groupLocationFragment,R.id.groupManagementFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
    }
}