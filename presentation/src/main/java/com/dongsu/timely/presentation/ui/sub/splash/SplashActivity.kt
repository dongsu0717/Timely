package com.dongsu.timely.presentation.ui.sub.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dongsu.presentation.databinding.ActivitySplashBinding
import com.dongsu.timely.presentation.ui.main.TimelyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, TimelyActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}