package com.dongsu.timely.presentation.ui.main.group.location

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentGroupLocationBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.map.TargetLocation
import com.dongsu.timely.domain.model.map.UserMeeting
import com.dongsu.timely.presentation.common.BaseTabFragment
import com.dongsu.timely.presentation.common.CommonUtils.calculateDistance
import com.dongsu.timely.presentation.common.CommonUtils.toastShort
import com.dongsu.timely.presentation.common.DialogUtils
import com.dongsu.timely.presentation.common.GET_ERROR
import com.dongsu.timely.presentation.common.GET_LOADING
import com.dongsu.timely.presentation.viewmodel.group.GroupLocationViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo
import com.kakao.vectormap.animation.Interpolation
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.mapwidget.InfoWindowLayer
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiImage
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import com.kakao.vectormap.shape.DotPoints
import com.kakao.vectormap.shape.Polygon
import com.kakao.vectormap.shape.PolygonOptions
import com.kakao.vectormap.shape.ShapeLayer
import com.kakao.vectormap.shape.ShapeManager
import com.kakao.vectormap.shape.animation.CircleWave
import com.kakao.vectormap.shape.animation.CircleWaves
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupLocationFragment :
    BaseTabFragment<FragmentGroupLocationBinding>(FragmentGroupLocationBinding::inflate) {

    private val groupLocationViewModel: GroupLocationViewModel by viewModels()
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap


    private val startZoomLevel = 15
    private val startPosition = LatLng.from(37.46819965686225, 126.90119500104446) //우리집임 ㅋㅋ
    private var locationScheduleId: Int? = null
    private var myId: Int? = null

    private lateinit var circleWavePolygon: Polygon
    private lateinit var shapeManager: ShapeManager
    private lateinit var shapeLayer: ShapeLayer
    private lateinit var infoWindowLayer: InfoWindowLayer

    override fun initView() {
        Log.e("위치프래그먼트", groupId.toString())
        checkStartMap()
    }

    private val readyCallback: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            // 인증 후 API 가 정상적으로 실행될 때 호출됨
            Log.i("k3f", "startPosition: " + kakaoMap.cameraPosition!!.position.toString())
            Log.i("k3f", "startZoomLevel: " + kakaoMap.zoomLevel)
//            val labelManager = kakaoMap.labelManager
//            val shapeManager = kakaoMap.shapeManager
//            val routeLineManager = kakaoMap.routeLineManager
//            val mapWidgetManager = kakaoMap.mapWidgetManager
//            val trackingManager = kakaoMap.trackingManager

            shapeManager = kakaoMap.shapeManager!!
            shapeLayer = shapeManager.layer
            infoWindowLayer = kakaoMap.mapWidgetManager!!.infoWindowLayer
            lifecycleScope.launch {
                getGroupMeetingInfoOnMap(kakaoMap)
            }


            kakaoMap.setOnInfoWindowClickListener { kakaoMap, infoWindow, guiId ->
                DialogUtils.showInputDialog(context = requireContext()) { inputText ->
                    updateStateMessage(inputText)
                    toastShort(requireContext(), "입력된 텍스트: $inputText")
                }
            }
            //라벨 스타일 생성
//            val styles = labelManager?.addLabelStyles(
//                LabelStyles.from(
//                    LabelStyle.from(R.drawable.red_dot_marker).setTextStyles(32,
//                Color.RED)))
//
//            val myPos = LatLng.from(37.46819965686225, 126.90119500104446)
//
//            // 2. LabelOptions 생성하기
//            val options = LabelOptions.from(myPos).setStyles(styles)
//
//            // 3. LabelLayer 가져오기 (또는 커스텀 Layer 생성)
//            val layer = labelManager?.layer
//
//            // 4. LabelLayer 에 LabelOptions 을 넣어 Label 생성하기
//            val label: Label = layer!!.addLabel(options)
//

        }

        override fun getPosition(): LatLng = startPosition // 지도 시작 시 위치 좌표를 설정
        override fun getZoomLevel(): Int = startZoomLevel // 지도 시작 시 확대/축소 줌 레벨 설정
        override fun getMapViewInfo(): MapViewInfo = super.getMapViewInfo() // 지도 타입
        override fun getViewName(): String = "MyFirstMap" // KakaoMap 의 고유한 이름을 설정
        override fun isVisible(): Boolean = true // 지도 시작 시 visible 여부를 설정
        override fun getTag(): String = "FirstMapTag" // KakaoMap 의 tag 을 설정
        override fun isDev(): Boolean = super.isDev()
        override fun getTimeout(): Int = super.getTimeout()
    }

    // MapLifeCycleCallback 을 통해 지도의 LifeCycle 관련 이벤트를 수신할 수 있다.
    private val lifeCycleCallback: MapLifeCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapResumed() = super.onMapResumed()
        override fun onMapPaused() = super.onMapPaused()
        override fun onMapDestroy() =
            toastShort(requireContext(), "onMapDestroy") // 지도 API 가 정상적으로 종료될 때 호출됨

        override fun onMapError(error: java.lang.Exception) =
            toastShort(requireContext(), "onMapError")  // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
    }

    private fun checkStartMap() {
        lifecycleScope.launch {
            val scheduleId = getScheduleIdShowMap()
            locationScheduleId = scheduleId
            myId = getMyId()
            Log.e("GroupLocationFragment", "가져올 scheduleId: $scheduleId")
            if (scheduleId != null && scheduleId != -1) {
                Log.e("GroupLocationFragment", "맵 열릴 scheduleId: $scheduleId")
                binding.tvNothingScheduleTime.visibility = View.GONE
                mapView = binding.mapView
                mapView.start(lifeCycleCallback, readyCallback)
                groupLocationViewModel.getParticipationMemberLocation(scheduleId)
            }
        }
    }

    private suspend fun getScheduleIdShowMap(): Int? =
        groupLocationViewModel.getScheduleIdShowMap(groupId)

    private suspend fun getMyId() {
        groupLocationViewModel.fetchMyUserId()
        groupLocationViewModel.myUserId.collectLatest { result ->
            when (result) {
                is TimelyResult.Loading -> {

                }
                is TimelyResult.Success -> {
                    myId = result.resultData
                }
                is TimelyResult.Empty -> {

                }
                is TimelyResult.NetworkError -> {

                }
                else -> {}
            }
        }
    }

    private suspend fun getGroupMeetingInfoOnMap(kakaoMap: KakaoMap) {
            groupLocationViewModel.groupMembersLocation.collectLatest { meetingInfo -> //멤버들 가져오기
                when (meetingInfo) {
                    is TimelyResult.Success -> {
                        val targetLocation = meetingInfo.resultData.targetLocation
                        val myLocation = meetingInfo.resultData.userMeetingData.find { it.user.userId == myId }?.location
                        Log.e("약속장소 가져오기", meetingInfo.resultData.targetLocation.toString())
                        Log.e("멤버들 가져오기", meetingInfo.resultData.userMeetingData.toString())
                        Log.e("내위치 가져오기", myLocation.toString())

                        if (myLocation != null) {
                            val distance = calculateDistance(
                                myLocation.latitude,
                                myLocation.longitude,
                                targetLocation.coordinate.latitude,
                                targetLocation.coordinate.longitude
                            )

                            Log.d("DistanceCheck", "Distance to target: $distance meters")

                            if (distance <= 100) {
                                arrivedPlace()
                                // 100미터 안에 들어온 경우 지도 닫기
                                closeKakaoMap()
                                return@collectLatest
                            }
                        }
                        updateAppointPlaceOnMap(kakaoMap, meetingInfo.resultData.targetLocation)
                        updateMemberLocationsOnMap(kakaoMap, meetingInfo.resultData.userMeetingData)

                    }

                    is TimelyResult.Loading -> {
                        toastShort(requireContext(), GET_LOADING)
                    }

                    is TimelyResult.NetworkError -> {
                        toastShort(requireContext(), GET_ERROR)
                        Log.e("멤버들 가져오기", meetingInfo.exception.toString())
                    }

                    else -> {
                        toastShort(requireContext(), "참여자 위치 가져오는 곳 ")
                    }
                }
            }

    }

    private fun updateMemberLocationsOnMap(kakaoMap: KakaoMap, meetingInfo: List<UserMeeting>) {
        val labelManager = kakaoMap.labelManager
        meetingInfo.forEach { member ->
            makeUserLabel(member, labelManager)
            makeUserStateMessage(member, labelManager)
        }
    }

    private fun updateStateMessage(stateMessage: String) {
        lifecycleScope.launch {
            locationScheduleId?.let { groupLocationViewModel.updateStateMessage(it, stateMessage) }
        }
    }

    private fun makeUserLabel(member: UserMeeting, labelManager: LabelManager?) {
        val labelStyle = LabelStyles.from(
            LabelStyle.from(R.drawable.current_location).setTextStyles(32, Color.BLACK)
        )
        val location = LatLng.from(member.location.latitude, member.location.longitude)
        val labelTextBuilder = LabelTextBuilder().setTexts(member.user.nickname)
        val labelOptions = LabelOptions.from(location)
            .setStyles(labelStyle)
            .setTexts(labelTextBuilder)
        labelManager?.layer?.addLabel(labelOptions)
        createAnimator(location, member.user.userId.toString())

        val id = member.user.userId.toString()
        infoWindowLayer.addInfoWindow(
            getStateMessageLayout(
                messageId = id,
                stateMessage = member.stateMessage,
                location = location
            )
        )
        infoWindowLayer.getInfoWindow(id).show()
    }

    private fun makeUserStateMessage(member: UserMeeting, labelManager: LabelManager?) {

    }

    private fun updateAppointPlaceOnMap(kakaoMap: KakaoMap, meetingInfo: TargetLocation) {
        val labelStyle = LabelStyles.from(
            LabelStyle.from(R.drawable.blue_marker).setTextStyles(22, Color.BLACK)
        )
        val placeLocation =
            LatLng.from(meetingInfo.coordinate.latitude, meetingInfo.coordinate.longitude)
        val placeName = LabelTextBuilder().setTexts(meetingInfo.location)
        val labelOptions = LabelOptions.from(placeLocation)
            .setStyles(labelStyle)
            .setTexts(placeName)
        kakaoMap.labelManager?.layer?.addLabel(labelOptions)

        val id = meetingInfo.location
        createAnimator(placeLocation, animatorId = id)
    }

    private fun createAnimator(location: LatLng?, animatorId: String) {
        val circleWaves = CircleWaves.from(animatorId)
            .setDuration(3000)
            .setRepeatCount(1000)
            .setInterpolation(Interpolation.CubicIn)
            .addCircleWave(CircleWave.from(0.7f, 0.0f, 0.0f, 100.0f))
        shapeManager.addAnimator(circleWaves)
        val circle: Polygon = shapeLayer.addPolygon(
            getCircleAnimationOptions(
                location,
                1
            )
        )
        shapeManager.getAnimator(animatorId).addPolygons(circle)
        shapeManager.getAnimator(animatorId).start()
    }

    private fun getCircleAnimationOptions(center: LatLng?, radius: Int): PolygonOptions {
        return PolygonOptions.from(
            DotPoints.fromCircle(center, radius.toFloat()),
            Color.parseColor("#078c03")
        )
    }

    private fun getStateMessageLayout(
        messageId: String,
        stateMessage: String? = "가는중",
        location: LatLng,
    ): InfoWindowOptions {
        val body = GuiLayout(Orientation.Horizontal)
        body.setPadding(20, 20, 20, 18)

        val image = GuiImage(R.drawable.window_body, true)
        image.setFixedArea(7, 7, 7, 7)
        body.setBackground(image)

//        val text = GuiText("가는중") //테스트용
        val text = GuiText(stateMessage) //실제로 들어갈 값
        text.setTextSize(30)
        body.addView(text)

        val options = InfoWindowOptions.from(
            messageId,
            location
        )
        options.setBody(body)
        options.setBodyOffset(0f, -4f)
        options.setTail(GuiImage(R.drawable.window_tail, false))
        options.setTailOffset(0f, -50f)
        options.setVisible(false)
        return options
    }
    private fun arrivedPlace(){

    }

    private fun closeKakaoMap(){
        toastShort(requireContext(), "약속 장소에 도착했습니다. 지도를 닫습니다.")
        binding.mapView.visibility = View.GONE // 지도를 숨김
        mapView.finish()
    }
}
