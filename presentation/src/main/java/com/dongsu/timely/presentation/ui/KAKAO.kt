//package com.dongsu.timely.presentation.ui
//
//package com.example.dongsu
//
//import android.content.ContentValues.TAG
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.kakao.sdk.auth.model.OAuthToken
//import com.kakao.sdk.common.model.ClientError
//import com.kakao.sdk.common.model.ClientErrorCause
//import com.kakao.sdk.common.util.Utility
//import com.kakao.sdk.user.UserApiClient
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Call
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.Body
//import retrofit2.http.GET
//import retrofit2.http.Header
//import retrofit2.http.POST
//import retrofit2.http.Query
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var button: Button
//
//    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//        if (error != null) {
//            Log.e(TAG, "카카오계정으로 로그인 실패", error)
//        } else if (token != null) {
//            Log.e(TAG, "카카오계정으로 로그인 성공22 $token")
//            // 카카오 인증 서버에 접근할 수 있는 코드를 가져오는 요청을 실행
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        button = findViewById(R.id.btn_kakao)
//
//        val hashKey = Utility.getKeyHash(this)
//        Log.d("hash",hashKey)
//        initView()
//
//        // 카카오 로그인
//
//        // 카카오계정으로 로그인 공통 callback 구성
//        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용
//
//    }
//    private fun initView() {
//
//        // 버튼 클릭했을 때 로그인
//
//        button.setOnClickListener {
//            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
//            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@MainActivity)) {
//                UserApiClient.instance.loginWithKakaoTalk(this@MainActivity) { token, error ->
//                    if (error != null) {
//                        Log.e(TAG, "카카오톡으로 로그인 실패", error)
//
//                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우
//                        // 의도적으로 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리(ex. 뒤로가기)
//                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                            return@loginWithKakaoTalk
//                        }
//
//                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
//                        UserApiClient.instance.loginWithKakaoAccount(
//                            this@MainActivity,
//                            callback = callback
//                        )
//                    } else if (token != null) {
//                        Log.e(TAG,"로그인 성공 $token")
//                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
//                        sendCodeToServer(token.accessToken)
//                    }
//                }
//            } else {
//                UserApiClient.instance.loginWithKakaoAccount(
//                    this@MainActivity,
//                    callback = callback
//                )
//            }
//        }
//    }
//    private fun saveLoginStatus(accessToken: String) {
//        val sharedPreferences: SharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("access_token", accessToken)
//        editor.putBoolean("is_logged_in", true)
//        editor.apply()
//    }
//    private fun sendCodeToServer(code: String) {
//        RetrofitClient.apiService.sendCode(code).enqueue(object : retrofit2.Callback<TokenResponse> {
//            override fun onResponse(call: Call<TokenResponse>, response: retrofit2.Response<TokenResponse>) {
//                if (response.isSuccessful) {
//                    val newAccessToken = response.body()!!.accessToken
//                    Log.e("발급받은 토큰",newAccessToken)
//                    saveLoginStatus(newAccessToken)
//                    Log.i(TAG, "코드 전송 성공")
//                    goMainTwo()
//                } else {
//                    Log.e(TAG, "코드 전송 실패: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//                Log.e(TAG, "서버 요청 실패", t)
//            }
//        })
//    }
//    fun goMainTwo() {
//        // 로그인 -> 성공 화면
//        startActivity(Intent(this, MainActivity2::class.java))
//    }
//    //  qdAYTEBYKRcuaJmBEkLtgBF2WRs=
//    //  qdAYTEBYKRcuaJmBEkLtgBF2WRs=
//
//    //종환
//    //ee0d873f3b14b4a89b04a43d24b5b421
//
//
//}
//interface ApiService {
//    @GET("auth/callback/kakao") // 서버의 엔드포인트 URL
//    fun sendCode(@Query("code") code: String): Call<TokenResponse> // JSON 형식으로 전송
//
//    @POST("test") // 서버의 해당 엔드포인트
//    fun sendTextData(@Header("Authorization") token: String, @Body request: TextRequest): Call<Void>
////    fun sendTextData(@Body request: TextRequest): Call<TextResponse>
//
//}
//data class TokenResponse(
//    val accessToken: String
//)
//
//
//data class TextRequest(
//    val text: String // 서버에 보낼 텍스트 데이터
//)
//
//
//// 데이터 클래스 정의
//
//object RetrofitClient {
//    private const val BASE_URL = "http://54.180.158.225:8080/" // API 기본 URL
//
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(
//                OkHttpClient.Builder()
//                    .addInterceptor(HttpLoggingInterceptor().apply {
//                        level = HttpLoggingInterceptor.Level.BODY
//                    }).build())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val apiService: ApiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }
//}