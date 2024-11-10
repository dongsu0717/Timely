package com.dongsu.timely.presentation.kakao

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.dongsu.timely.presentation.common.KAKAO_TAG
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient


class KaKaoLoginManager(
    private val activity: Activity,
    private val loginCallback: (OAuthToken) -> Unit
) {
    fun initiateKakaoLogin() {
        if (isKakaoTalkLoginAvailable()) {

            gologinWithKakaoTalk()
        } else {
            loginWithKakaoAccount()
        }
    }

    private fun isKakaoTalkLoginAvailable(): Boolean =
        UserApiClient.instance.isKakaoTalkLoginAvailable(activity)

    private fun gologinWithKakaoTalk() =
        UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
            if (error != null) {
                Log.e(KAKAO_TAG, "카카오톡으로 로그인 실패", error)
                if (isCancelledError(error)) {
                    return@loginWithKakaoTalk
                }
                loginWithKakaoAccount()
            } else if(token != null) {
                Log.e(KAKAO_TAG, "카카오로그인 성공 ${token.accessToken}")
                loginCallback(token)
            }
        }

    private fun loginWithKakaoAccount() =
        UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)

    private fun isCancelledError(error: Throwable): Boolean =
        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
            Log.e(ContentValues.TAG, "캔슬에러", error)
            true
        } else false

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(KAKAO_TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.e(KAKAO_TAG, "카카오계정으로 로그인 성공, ${token.accessToken}")
            loginCallback(token)
        }
    }

}