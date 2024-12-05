package com.dongsu.timely.presentation.ui.main.group.location

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentGroupLocationBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.map.GroupMeetingInfo
import com.dongsu.timely.domain.model.map.TargetLocation
import com.dongsu.timely.domain.model.map.UserMeeting
import com.dongsu.timely.presentation.common.BaseTabFragment
import com.dongsu.timely.presentation.common.CommonUtils.calculateDistance
import com.dongsu.timely.presentation.common.CommonUtils.toastShort
import com.dongsu.timely.presentation.common.DEFAULT_START_ZOOM_LEVEL
import com.dongsu.timely.presentation.common.DISTANCE_TO_ARRIVE_POINT
import com.dongsu.timely.presentation.common.DialogUtils
import com.dongsu.timely.presentation.common.FAIL_SEND_STATE_MESSAGE
import com.dongsu.timely.presentation.common.GET_EMPTY
import com.dongsu.timely.presentation.common.GET_ERROR
import com.dongsu.timely.presentation.common.GET_LOADING
import com.dongsu.timely.presentation.common.SUCCESS_SEND_STATE_MESSAGE
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

    private val startZoomLevel = DEFAULT_START_ZOOM_LEVEL
    private val startPosition = LatLng.from(37.46819965686225, 126.90119500104446) //우리집임 ㅋㅋ
    private var scheduleIdToShowMap: Int? = null
    private var myId: Int? = null

    private lateinit var shapeManager: ShapeManager
    private lateinit var shapeLayer: ShapeLayer
    private lateinit var infoWindowLayer: InfoWindowLayer

    override fun initView() {
        getScheduleIdToShowMap()
    }

    private val readyCallback: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            Log.i("k3f", "startPosition: " + kakaoMap.cameraPosition!!.position.toString())
            Log.i("k3f", "startZoomLevel: " + kakaoMap.zoomLevel)

            shapeManager = kakaoMap.shapeManager!!
            shapeLayer = shapeManager.layer
            infoWindowLayer = kakaoMap.mapWidgetManager!!.infoWindowLayer
            getGroupMeetingInfoOnMap(kakaoMap)

            kakaoMap.setOnInfoWindowClickListener { kakaoMap, infoWindow, guiId ->
                DialogUtils.showInputDialog(context = requireContext()) { inputText ->
                    updateStateMessage(inputText)
                    toastShort(requireContext(), "입력된 텍스트: $inputText")
                }
            }
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

    private fun getScheduleIdToShowMap() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                groupLocationViewModel.fetchGroupScheduleShowMap(groupId)
                groupLocationViewModel.groupScheduleToShowMap.collectLatest { result ->
                    when (result) {
                        is TimelyResult.Loading -> {
                            Log.e("맵에 보여줄 스케줄", "로딩중")
                        }

                        is TimelyResult.Success -> {
                            setScheduleIdToShowMap(result.resultData.scheduleId)
                            setBeforeStartMap()
                        }

                        is TimelyResult.Empty -> {
                            Log.e("맵에 보여줄 스케줄", "비어있음. empty라고")
                        }

                        is TimelyResult.NetworkError -> {
                            Log.e("맵에 보여줄 스케줄", "에러뜸")
                        }

                        else -> {
                            Log.e("맵에 보여줄 스케줄", "니가왜떠")
                        }
                    }
                }
            }
        }
    }

    private fun setScheduleIdToShowMap(scheduleId: Int) {
        scheduleIdToShowMap = scheduleId
        Log.e("GroupLocationFragment", "맵에 보여줄 scheduleId - $scheduleIdToShowMap")
    }

    private fun setBeforeStartMap() {
        if (scheduleIdToShowMap != null && scheduleIdToShowMap != -1) {
            textGone()
            getMyId()
            startMap()
            getGroupMemberLocation(scheduleIdToShowMap!!)
        }
    }

    private fun textGone() {
        binding.tvNothingScheduleTime.visibility = View.GONE
    }

    private fun getMyId() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupLocationViewModel.fetchMyUserId()
                groupLocationViewModel.myUserId.collectLatest { result ->
                    when (result) {
                        is TimelyResult.Loading -> {
                            Log.e("GroupLocationFragment", "내아이디 가져오기 - 로딩중")
                        }

                        is TimelyResult.Success -> {
                            Log.e("GroupLocationFragment", "내아이디 가져오기 - 성공")
                            myId = result.resultData
                        }

                        is TimelyResult.Empty -> {
                            Log.e("GroupLocationFragment", "내아이디 가져오기 - 비어있음")
                        }

                        is TimelyResult.NetworkError -> {
                            Log.e("GroupLocationFragment", "내아이디 가져오기 - 네트워크 오류")
                        }

                        else -> {}
                    }
                }

            }
        }
    }

    private fun getGroupMemberLocation(scheduleId: Int) =
        groupLocationViewModel.fetchGroupMeetingInfo(scheduleId)


    private fun getGroupMeetingInfoOnMap(kakaoMap: KakaoMap) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupLocationViewModel.groupMeetingInfo.collectLatest { meetingInfo ->
                    when (meetingInfo) {
                        is TimelyResult.Loading -> {
                            toastShort(requireContext(), GET_LOADING)
                        }

                        is TimelyResult.Success -> {
                            setGroupMeetingInfoOnMap(kakaoMap, meetingInfo.resultData)
                        }

                        is TimelyResult.Empty -> {
                            toastShort(requireContext(), GET_EMPTY)
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
        }
    }

    private fun setGroupMeetingInfoOnMap(kakaoMap: KakaoMap, meetingInfo: GroupMeetingInfo) {
        val targetLocation = meetingInfo.targetLocation
        Log.e("약속장소 가져오기", targetLocation.toString())

        val usersLocation = meetingInfo.userMeetingData
        Log.e("멤버들 가져오기", usersLocation.toString())

        updateAppointPlaceOnMap(kakaoMap, targetLocation)
        updateMemberLocationsOnMap(kakaoMap, usersLocation)
        checkIsArrive(usersLocation,targetLocation)
    }

    private fun startMap() {
        mapView = binding.mapView
        mapView.start(lifeCycleCallback, readyCallback)
    }

    private fun getMyLocation(usersLocation: List<UserMeeting>) =
        usersLocation.find { it.user.userId == myId }?.location

    private fun checkIsArrive(usersLocation: List<UserMeeting>, targetLocation: TargetLocation) {
        val myLocation = getMyLocation(usersLocation)
        Log.e("내위치 가져오기", myLocation.toString())

        if (myLocation != null) {
            val distance = calculateDistance(
                myLocation.latitude,
                myLocation.longitude,
                targetLocation.coordinate.latitude,
                targetLocation.coordinate.longitude
            )
            Log.d("목적지까지 남은 거리", "목적지까지 남은 거리 ${distance}m")

            if (distance <= DISTANCE_TO_ARRIVE_POINT) {
                arrivedPlace()
                closeKakaoMap()
            }
        }
    }

    private fun updateMemberLocationsOnMap(kakaoMap: KakaoMap, meetingInfo: List<UserMeeting>) {
        val labelManager = kakaoMap.labelManager
        meetingInfo.forEach { member ->
            makeUserLabel(member, labelManager)
        }
    }

    private fun updateStateMessage(stateMessage: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scheduleIdToShowMap?.let { groupLocationViewModel.updateStateMessage(it, stateMessage) }
                groupLocationViewModel.stateMessage.collectLatest { result ->
                    when (result) {
                        is TimelyResult.Loading -> {

                        }

                        is TimelyResult.Success -> {
                            toastShort(requireContext(), SUCCESS_SEND_STATE_MESSAGE)
                        }

                        is TimelyResult.Empty -> {

                        }

                        is TimelyResult.NetworkError -> {
                            toastShort(requireContext(), FAIL_SEND_STATE_MESSAGE)
                        }

                        else -> {}
                    }
                }
            }
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
                stateMessage = member.stateMessage?:"가는중",
                location = location
            )
        )
        infoWindowLayer.getInfoWindow(id).show()
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
        stateMessage: String,
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

    private fun arrivedPlace() {

    }

    private fun closeKakaoMap() {
        toastShort(requireContext(), "약속 장소에 도착했습니다. 지도를 닫습니다.")
        binding.mapView.visibility = View.GONE
        binding.tvNothingScheduleTime.visibility = View.VISIBLE
        mapView.finish()
    }
}
