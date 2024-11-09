//package com.dongsu.timely.presentation.ui
//
//import com.dongsu.presentation.R
//
//package com.example.dongsu
//
//import android.content.ActivityNotFoundException
//import android.content.ContentValues
//import android.content.ContentValues.TAG
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.dongsu.databinding.ActivityMain2Binding
//import com.example.dongsu.databinding.ActivityMainBinding
//import com.kakao.sdk.common.util.KakaoCustomTabsClient
//import com.kakao.sdk.share.ShareClient
//import com.kakao.sdk.share.WebSharerClient
//import com.kakao.sdk.template.model.Content
//import com.kakao.sdk.template.model.FeedTemplate
//import com.kakao.sdk.template.model.ItemContent
//import com.kakao.sdk.template.model.ItemInfo
//import com.kakao.sdk.template.model.Link
//import com.kakao.sdk.template.model.Social
//import com.kakao.sdk.template.model.TextTemplate
//import com.kakao.sdk.user.UserApiClient
//import retrofit2.Call
//
//class MainActivity2 : AppCompatActivity() {
//
//    private lateinit var textId: TextView
//    private lateinit var textNick: TextView
//    private lateinit var button: Button
//    private lateinit var buttonSend: Button
//    private lateinit var buttonMe: Button
//    private lateinit var buttonFriend: Button
//    private lateinit var editText: EditText
//    private var accessToken: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main2)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        if (!isUserLoggedIn()) {
//            navigateToLoginScreen()
//            return
//        }
//        accessToken = getAccessToken()
//        if (accessToken != null) {
//            Log.e("2에서 저장된 토큰 ", "저장된 토큰: $accessToken")
//            // 서버에 요청을 보낼 때, 이 토큰을 사용
//        } else {
//            Log.e("Token", "저장된 토큰이 없습니다.")
//        }
//        textId = findViewById(R.id.tv_login_id)
//        textNick = findViewById(R.id.tv_login_nickname)
//        button = findViewById(R.id.btn_bb)
//        buttonSend = findViewById(R.id.btn_send)
//        editText = findViewById(R.id.et)
//        buttonMe = findViewById(R.id.btn_send_me)
//        buttonFriend = findViewById(R.id.btn_send_friend)
//
//        UserApiClient.instance.me { user, error ->
//            if (error != null) {
////                Log.e(ContentValues.TAG,user.id.toString())
//                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
//            } else if (user != null) {
//                Log.i(ContentValues.TAG, "사용자 정보 요청 성공")
//                textId.text = user.id.toString()
//                textNick.text = user.kakaoAccount?.profile?.nickname.toString()
//            }
//        }
//        button.setOnClickListener {
//            UserApiClient.instance.unlink { error ->
//                if (error != null) {
//                    Log.e(ContentValues.TAG, "회원 탈퇴 실패", error)
//                } else {
//                    Log.i(ContentValues.TAG, "회원 탈퇴 성공. 해당 계정의 모든 데이터가 삭제되었습니다.")
//                    // 탈퇴 후 필요한 화면 전환 또는 메시지 표시
//                    finish() // 현재 Activity 종료
//                }
//            }
//        }
//        buttonSend.setOnClickListener {
//            val requestText = editText.text.toString()
//            val request = TextRequest(requestText)
//            sendTextToServer(accessToken!!,request)
//        }
//        buttonMe.setOnClickListener {
//
//        }
//        buttonFriend.setOnClickListener {
//            val defaultText = TextTemplate(
//                text = """
//        카카오톡 공유는 카카오톡을 실행하여
//        사용자가 선택한 채팅방으로 메시지를 전송합니다.
//    """.trimIndent(),
//                link = Link(
//                    webUrl = "https://developers.kakao.com",
//                    mobileWebUrl = "https://developers.kakao.com"
//                )
//            )
//
//            // 피드 메시지 보내기
//
//// 카카오톡 설치여부 확인
//            if (ShareClient.instance.isKakaoTalkSharingAvailable(applicationContext)) {
//                // 카카오톡으로 카카오톡 공유 가능
//                ShareClient.instance.shareDefault(applicationContext, defaultText) { sharingResult, error ->
//                    if (error != null) {
//                        Log.e(TAG, "카카오톡 공유 실패", error)
//                    }
//                    else if (sharingResult != null) {
//                        Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
//                        startActivity(sharingResult.intent)
//
//                        // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
//                        Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
//                        Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")
//                    }
//                }
//            } else {
//                // 카카오톡 미설치: 웹 공유 사용 권장
//                // 웹 공유 예시 코드
//                val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultText)
//
//                // CustomTabs으로 웹 브라우저 열기
//
//                // 1. CustomTabsServiceConnection 지원 브라우저 열기
//                // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
//                try {
//                    KakaoCustomTabsClient.openWithDefault(applicationContext, sharerUrl)
//                } catch(e: UnsupportedOperationException) {
//                    // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
//                }
//
//                // 2. CustomTabsServiceConnection 미지원 브라우저 열기
//                // ex) 다음, 네이버 등
//                try {
//                    KakaoCustomTabsClient.open(applicationContext, sharerUrl)
//                } catch (e: ActivityNotFoundException) {
//                    // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
//                }
//            }
//        }
//    }
//
//    private fun sendTextToServer(token: String, request: TextRequest) {
//
//        RetrofitClient.apiService.sendTextData("Bearer $token",request).enqueue(object : retrofit2.Callback<Void> {
//            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
//                if (response.isSuccessful) {
//                    Log.i(TAG, "코드 전송 성공")
//                } else {
//                    Log.e(TAG, "코드 전송 실패: ${response.code()}")
//                }
//            }
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//    }
//    private fun isUserLoggedIn(): Boolean {
//        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
//        return sharedPreferences.getBoolean("is_logged_in", false)
//    }
//
//    private fun navigateToLoginScreen() {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish() // 메인 화면에서 로그인 화면으로 이동했으므로 현재 화면 종료
//    }
//    private fun getAccessToken(): String? {
//        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
//        return sharedPreferences.getString("access_token", null)
//    }
//}