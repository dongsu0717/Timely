package com.dongsu.timely.presentation.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        Log.e("TimelyActivity", "onCreate: ")
        binding = ActivityTimelyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        binding.bottomNavigation.setupWithNavController(navController)

    }
    fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
}