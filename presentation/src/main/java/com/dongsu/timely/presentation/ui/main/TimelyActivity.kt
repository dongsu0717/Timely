package com.dongsu.timely.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.common.GROUP_NAME
import com.dongsu.timely.common.SCHEDULE_ID
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.presentation.common.CommonDialogFragment
import com.dongsu.timely.presentation.common.INVITE_CODE
import com.dongsu.timely.presentation.common.LOGIN_MESSAGE
import com.dongsu.timely.presentation.common.LOGIN_NEGATIVE_BUTTON
import com.dongsu.timely.presentation.common.LOGIN_POSITIVE_BUTTON
import com.dongsu.timely.presentation.common.LOGIN_TITLE
import com.dongsu.timely.presentation.kakao.KaKaoLoginManager
import com.dongsu.timely.presentation.viewmodel.TimelyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TimelyActivity : AppCompatActivity() {

    private val timelyViewModel: TimelyViewModel by viewModels()
    private lateinit var kakaoLoginManager: KaKaoLoginManager
    private lateinit var navController: NavController
    private lateinit var binding: ActivityTimelyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = setNavController()
        setupBottomNavigation(navController)
        checkArgument(navController)
        setKaKaoLoginManager()
        checkInviteCodeToIntent()
    }

    private fun setNavController(): NavController =
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController

    private fun setupBottomNavigation(navController: NavController) {
        binding.bottomNavigation.setupWithNavController(navController)
        setBottomNavigationVisibility(navController)
        setupNavigation(navController)
    }

    private fun setBottomNavigationVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.visibility = when (destination.id) {
                R.id.calendarFragment, R.id.groupListFragment, R.id.profileFragment, R.id.groupDateFragment, R.id.groupLocationFragment, R.id.groupManagementFragment, R.id.groupPageFragment -> View.VISIBLE
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
                // else -> it.onNavDestinationSelected(navController)
            }
            it.onNavDestinationSelected(navController)
        }
    }

    private fun checkLoginStatus(callback: (Boolean) -> Unit) = lifecycleScope.launch {
        timelyViewModel.isLoggedIn()
        callback(timelyViewModel.loginStatus.value is TimelyResult.Success)
    }

    private fun setKaKaoLoginManager() {
        kakaoLoginManager = KaKaoLoginManager(this) { token ->
            lifecycleScope.launch {
                timelyViewModel.sendKaKaoTokenAndGetToken(token.accessToken)
                timelyViewModel.fetchToken.collectLatest { result ->
                    when (result) {
                        is TimelyResult.Loading -> {

                        }

                        is TimelyResult.Success -> {
                            saveTokenLocal(
                                result.resultData.accessToken,
                                result.resultData.refreshToken
                            )
                            sendFCMToken()
                        }

                        is TimelyResult.Empty -> {

                        }

                        is TimelyResult.NetworkError -> {

                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private suspend fun sendFCMToken() {
        timelyViewModel.sendFCMToken()
        timelyViewModel.sendFCMTokenState.collectLatest { result ->
            when (result) {
                is TimelyResult.Loading -> {

                }

                is TimelyResult.Success -> {
                    Log.e("fcm보내기", "성공")
                }

                is TimelyResult.Empty -> {

                }

                is TimelyResult.NetworkError -> {

                }

                else -> {}
            }
        }
    }

    private suspend fun saveTokenLocal(accessToken: String?, refreshToken: String?) {
        if (accessToken != null && refreshToken != null)
            timelyViewModel.saveTokenLocal(accessToken, refreshToken)
        timelyViewModel.saveTokenLocalState.collectLatest { result ->
            when (result) {
                is TimelyResult.Loading -> {

                }

                is TimelyResult.Success -> {
                    Log.e("TimelyActivitiy토큰저장", "성공")
                }

                is TimelyResult.Empty -> {

                }

                is TimelyResult.LocalError -> {
                    Log.e("TimelyActivitiy토큰저장", "localError")

                }

                else -> {}
            }
        }
    }

    private suspend fun checkIsLoggedIn() {
        timelyViewModel.isLoggedIn()
        timelyViewModel.loginStatus.collectLatest { result ->
            when (result) {
                is TimelyResult.Loading -> {
                    Log.e("TimelyActivity로그인확인", "로그인상태 로딩중임")

                }

                is TimelyResult.Success -> {
                    Log.e("TimelyActivity로그인확인", "로그인상태 success임")
                }

                is TimelyResult.Empty -> {
                    Log.e("TimelyActivity로그인확인", "로그인상태 empty임")

                }

                is TimelyResult.LocalError -> {
                    Log.e("TimelyActivity로그인확인", "로그인상태 localError뜸")

                }

                else -> {
                    Log.e("TimelyActivity로그인확인", "로그인상태 니가 왜떠")
                }
            }
        }
    }

    private fun showLoginDialog() = CommonDialogFragment(
        LOGIN_TITLE,
        LOGIN_MESSAGE,
        LOGIN_POSITIVE_BUTTON,
        LOGIN_NEGATIVE_BUTTON
    ) {
        kakaoLoginManager.initiateKakaoLogin()
    }.show(supportFragmentManager, "LoginDialogFragment")

    private fun showJoinGroupDialog(inviteCode: String) =
        CommonDialogFragment("그룹에 초대되었습니다.", "그룹에 가입하시겠습니까?", "가입하기", "취소") {
            lifecycleScope.launch {
                timelyViewModel.joinGroup(inviteCode = inviteCode)
            }
        }.show(supportFragmentManager, "JoinDialogFragment")

    private fun checkInviteCodeToIntent() {
        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            if (uri != null) {
                val inviteCode = uri.getQueryParameter(INVITE_CODE)
                Log.e("카카오초대", uri.getQueryParameter(GROUP_ID).toString())
                checkLoginStatus { isLoggedIn ->
                    if (!isLoggedIn) {
                        showLoginDialog()
                    } else {
                        if (inviteCode != null) {
                            showJoinGroupDialog(inviteCode)
                        }
                    }
                }
            }
        }
    }

    private fun checkArgument(navController: NavController) {
        val groupId = intent.getIntExtra(GROUP_ID, -1)
        val groupName = intent.getStringExtra(GROUP_NAME)
        val scheduleId = intent.getIntExtra(SCHEDULE_ID, -1)

        Log.e("timly", groupId.toString())
        if (groupId != -1) {
            goGroupDetail(navController, groupId, groupName, scheduleId)
        }
    }

    private fun goGroupDetail(
        navController: NavController,
        groupId: Int,
        groupName: String?,
        scheduleId: Int,
    ) {
        val bundle = Bundle().apply {
            putInt(GROUP_ID, groupId)
            groupName?.let { putString(GROUP_NAME, it) }
            if (scheduleId != -1) putInt(SCHEDULE_ID, scheduleId)
        }
        navController.navigate(R.id.groupPageFragment, bundle)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let { nonNullIntent ->
            val groupId = nonNullIntent.getIntExtra(GROUP_ID, -1)
            val groupName = nonNullIntent.getStringExtra(GROUP_NAME)
            val scheduleId = nonNullIntent.getIntExtra(SCHEDULE_ID, -1)

            if (groupId != -1) {
                goGroupDetail(navController, groupId, groupName, scheduleId)
            }
        }
    }
}