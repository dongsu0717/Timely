package com.dongsu.timely.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.ActivityTimelyBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.presentation.common.CommonDialogFragment
import com.dongsu.timely.presentation.common.LOGIN_MESSAGE
import com.dongsu.timely.presentation.common.LOGIN_NEGATIVE_BUTTON
import com.dongsu.timely.presentation.common.LOGIN_POSITIVE_BUTTON
import com.dongsu.timely.presentation.common.LOGIN_TITLE
import com.dongsu.timely.presentation.kakao.KaKaoLoginManager
import com.dongsu.timely.presentation.viewmodel.TimelyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TimelyActivity : AppCompatActivity() {

    private val timelyViewModel: TimelyViewModel by viewModels()
    private lateinit var kakaoLoginManager: KaKaoLoginManager

    private lateinit var binding: ActivityTimelyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimelyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
        setKaKaoLoginManager()
    }

    private fun setupBottomNavigation() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        binding.bottomNavigation.setupWithNavController(navController)
        setBottomNavigationVisibility(navController)
        setupNavigation(navController)
    }

    private fun setBottomNavigationVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.visibility = when (destination.id) {
                R.id.calendarFragment, R.id.groupListFragment, R.id.profileFragment, R.id.groupDateFragment, R.id.groupLocationFragment, R.id.groupManagementFragment, R.id.groupPageFragment-> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    private fun setupNavigation(navController: NavController) {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.groupListFragment -> {
                    checkLoginStatus { isLoggedIn ->
                        if (!isLoggedIn) {
                            showLoginDialog()
                        } else {
                            it.onNavDestinationSelected(navController)
                        }
                    }
                    return@setOnItemSelectedListener false
                }
            }
            it.onNavDestinationSelected(navController)
        }
    }

    private fun checkLoginStatus(callback: (Boolean) -> Unit) = lifecycleScope.launch {
            timelyViewModel.isLoggedIn()
            callback(timelyViewModel.loginStatus.value is TimelyResult.Success)
        }

    private fun setKaKaoLoginManager() {
        kakaoLoginManager = KaKaoLoginManager(this){ token ->
            lifecycleScope.launch {
                timelyViewModel.sendToken(token.accessToken)
            }
        }
    }

    private fun showLoginDialog() = CommonDialogFragment(LOGIN_TITLE, LOGIN_MESSAGE, LOGIN_POSITIVE_BUTTON, LOGIN_NEGATIVE_BUTTON){
        kakaoLoginManager.initiateKakaoLogin()
    }.show(supportFragmentManager, "LoginDialogFragment")

}