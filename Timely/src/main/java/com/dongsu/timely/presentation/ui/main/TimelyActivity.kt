package com.dongsu.timely.presentation.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dongsu.timely.R
import com.dongsu.timely.databinding.ActivityTimelyBinding

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
}