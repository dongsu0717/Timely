package com.dongsu.timely.presentation.ui.main.group.management

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dongsu.presentation.databinding.FragmentGroupManagementBinding
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.presentation.common.BaseTabFragment
import com.dongsu.timely.presentation.common.CommonUtils.toastShort
import com.dongsu.timely.presentation.common.GET_ERROR
import com.dongsu.timely.presentation.common.INVITE_CODE
import com.dongsu.timely.presentation.common.INVITE_GROUP_MESSAGE
import com.dongsu.timely.presentation.common.LOADING
import com.dongsu.timely.presentation.common.throttledClickListener
import com.dongsu.timely.presentation.viewmodel.group.GroupManagementViewModel
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupManagementFragment: BaseTabFragment<FragmentGroupManagementBinding>(FragmentGroupManagementBinding::inflate) {

    private val groupManagementViewModel: GroupManagementViewModel by viewModels()

    override fun initView() {
        Log.e("관리", groupId.toString())
        clickInviteMember()
    }

    private fun clickInviteMember() {
        binding.tvInviteGroup.throttledClickListener(lifecycleScope) {
            createInviteCode()
        }
    }

    private fun createInviteCode() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupManagementViewModel.createInviteCode(groupId)
                groupManagementViewModel.inviteCode.collectLatest { result ->
                    when (result) {
                        is TimelyResult.Loading -> {
                            Log.e("초대 코드 가져오기", "로딩중")
                            toastShort(requireContext(), LOADING)
                        }

                        is TimelyResult.Success -> {
                            Log.e("초대 코드 가져오기", "성공")

                            with(result.resultData) {
                                sendInviteMessageKaKaoTalk(groupId, inviteCode)
                            }
                        }

                        is TimelyResult.Empty -> {
                            Log.e("초대 코드 가져오기", "비어있음")
                        }

                        is TimelyResult.NetworkError -> {
                            Log.e("초대 코드 가져오기", "에러. 실패임")
                            toastShort(requireContext(), GET_ERROR)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun sendInviteMessageKaKaoTalk(groupId: Int, inviteCode: String) {

        val inviteTextTemplate = TextTemplate(
            text = INVITE_GROUP_MESSAGE
                .trimIndent(),
            link = Link(
                androidExecutionParams = mapOf(
                    GROUP_ID to groupId.toString(),
                    INVITE_CODE to inviteCode
                )
            )
        )
        // 카카오톡 설치여부 확인
        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            // 카카오톡으로 카카오톡 공유 가능

            ShareClient.instance.shareDefault(
                requireContext(),
                inviteTextTemplate
            ) { sharingResult, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡 공유 실패", error)
                } else if (sharingResult != null) {
                    Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                    startActivity(sharingResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
                    Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(inviteTextTemplate)

            // CustomTabs으로 웹 브라우저 열기

            // 1. CustomTabsServiceConnection 지원 브라우저 열기
            // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
            try {
                KakaoCustomTabsClient.openWithDefault(requireContext(), sharerUrl)
            } catch (e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabsServiceConnection 미지원 브라우저 열기
            // ex) 다음, 네이버 등
            try {
                KakaoCustomTabsClient.open(requireContext(), sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }
}