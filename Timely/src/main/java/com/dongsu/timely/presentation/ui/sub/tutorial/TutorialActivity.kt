package com.dongsu.timely.presentation.ui.sub.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dongsu.timely.databinding.ActivityTutorialBinding
import com.dongsu.timely.presentation.ui.sub.login.LoginActivity
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class TutorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialBinding

    @OptIn(FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            sivSkipTutorial.clicks()
                .debounce(2000)
                .onEach {
                    val intent = Intent(this@TutorialActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }.launchIn(lifecycleScope)
        }

    }
}