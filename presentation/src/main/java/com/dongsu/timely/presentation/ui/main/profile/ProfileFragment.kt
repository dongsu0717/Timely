package com.dongsu.timely.presentation.ui.main.profile

import android.content.ContentValues
import android.util.Log
import com.bumptech.glide.Glide
import com.dongsu.presentation.databinding.FragmentProfileBinding
import com.dongsu.timely.presentation.common.BaseFragment
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User

class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun initView() {
        withDraw()
        setProfile()
    }

    private fun withDraw() {
        binding.tvWithdraw.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "회원 탈퇴 실패", error)
                } else {
                    Log.i(ContentValues.TAG, "회원 탈퇴 성공. 해당 계정의 모든 데이터가 삭제되었습니다.")
                    // 탈퇴 후 필요한 화면 전환 또는 메시지 표시
                }
            }
        }
    }

    private fun setProfile() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(ContentValues.TAG, "사용자 정보 요청 성공 $user")
                setNickname(user)
                setProfileImage(user)
            }
        }
    }

    private fun setNickname(user: User) {
        binding.textNickname.text = user.kakaoAccount?.profile?.nickname.toString()
    }

    private fun setProfileImage(user: User) {
        Glide.with(this)
            .load(user.kakaoAccount?.profile?.profileImageUrl)
            .into(binding.imageProfile)
    }

}
