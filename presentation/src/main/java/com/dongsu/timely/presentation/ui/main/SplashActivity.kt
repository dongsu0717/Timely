package com.dongsu.timely.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dongsu.presentation.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
//    private val calendarViewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("SplashActivity", "onCreate")
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        getSchedule()
        moveCalendar()
    }
/*

    private fun getSchedule() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                calendarViewModel.loadAllSchedule()
                calendarViewModel.scheduleList.collectLatest { result ->
                    when (result) {
                        is TimelyResult.Loading -> {
                            Log.e("SplashActivity", "리스트 가져오기 - Loading")
                        }
                        is TimelyResult.Success -> {
                            Log.e("SplashActivity", "리스트 가져오기 - Success")
                            moveCalendar()
                        }
                        is TimelyResult.Empty -> {
                            Log.e("SplashActivity", "리스트 가져오기 - Empty")
                            moveCalendar()
                        }
                        else -> {}

                    }
                }
            }
        }
    }
*/

    private fun moveCalendar() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, TimelyActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }

    override fun onResume() {
        super.onResume()
        Log.e("SplashActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("SplashActivity", "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("SplashActivity", "onDestroy")
    }
}