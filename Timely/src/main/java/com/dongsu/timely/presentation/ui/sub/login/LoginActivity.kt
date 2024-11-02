package com.dongsu.timely.presentation.ui.sub.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dongsu.timely.databinding.ActivityLoginBinding
import com.dongsu.timely.presentation.ui.main.TimelyActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.subscribe
import reactivecircus.flowbinding.android.view.clicks

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            tvUseNotLogin.clicks()
                .onEach {
                    val intent = Intent(this@LoginActivity, TimelyActivity::class.java)
                    startActivity(intent)
                    finish()
                }.launchIn(lifecycleScope)
        }
    }
}