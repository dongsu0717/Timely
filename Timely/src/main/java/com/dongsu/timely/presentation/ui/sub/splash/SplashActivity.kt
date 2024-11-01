package com.dongsu.timely.presentation.ui.sub.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dongsu.timely.databinding.ActivitySplashBinding
import com.dongsu.timely.presentation.ui.sub.tutorial.TutorialActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
            finish() // 스플래시 액티비티 종료
        }, 2000)
    }
}