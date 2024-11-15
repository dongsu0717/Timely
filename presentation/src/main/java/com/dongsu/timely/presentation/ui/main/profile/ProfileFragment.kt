package com.dongsu.timely.presentation.ui.main.profile

import android.content.ContentValues
import android.util.Log
import com.dongsu.presentation.databinding.FragmentProfileBinding
import com.dongsu.timely.presentation.common.BaseFragment
import com.kakao.sdk.user.UserApiClient

class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun initView() {
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
}
