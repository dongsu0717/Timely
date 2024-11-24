package com.dongsu.timely.presentation.ui.main.calendar.location

import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentSearchLocationBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.CommonUtils.toastShort
import com.dongsu.timely.presentation.viewmodel.calendar.location.SearchLocationViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.mapwidget.InfoWindowLayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchLocationFragment :
    BaseFragment<FragmentSearchLocationBinding>(FragmentSearchLocationBinding::inflate) {

    private val searchViewModel: SearchLocationViewModel by viewModels()
    private val args: SearchLocationFragmentArgs by navArgs()
    private lateinit var searchAdapter: SearchAdapter

    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap
    private lateinit var placeStartPosition: LatLng
    private lateinit var infoWindowLayer: InfoWindowLayer
    private val startZoomLevel = 15
    private val startPosition = LatLng.from(37.46819965686225, 126.90119500104446)

    override fun initView() {
        persistentBottomSheetEvent()
        setLayoutManager()
        setAdapter()
        search()
    }

    private fun persistentBottomSheetEvent() {
        behavior = BottomSheetBehavior.from(binding.persistentBottomSheet)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 슬라이드 되는 도중 계속 호출
                // called continuously while dragging
                Log.d(TAG, "onStateChanged: 드래그 중")
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Log.d(TAG, "onStateChanged: 접음")
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        Log.d(TAG, "onStateChanged: 드래그")
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Log.d(TAG, "onStateChanged: 펼침")
//                        val intent = Intent(this@PersistentExample1Activity, PlaceInfoActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                        startActivity(intent)
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.d(TAG, "onStateChanged: 숨기기")
                    }

                    BottomSheetBehavior.STATE_SETTLING -> {
                        Log.d(TAG, "onStateChanged: 고정됨")
                    }
                }
            }
        })
    }

    private val TAG = "PersistentActivity"
    private fun setLayoutManager() {
        binding.recyclerViewPoi.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setAdapter() {
        searchAdapter = SearchAdapter { setNaviGraph(it) }
        binding.recyclerViewPoi.adapter = searchAdapter
    }

    private fun setNaviGraph(poiItem: PoiItem) {
        if (args.groupId != 0) {
            goGroupAddSchedule(poiItem)
        } else {
            goAddSchedule(poiItem)
        }
    }

    private fun search() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mapView = binding.mapView
                mapView.start(lifeCycleCallback, readyCallback)
                query?.let { searchViewModel.searchLocation(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun getSearchLocationList(kakaoMap: KakaoMap) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.locationsList.collectLatest { results ->
                    when (results) {
                        is TimelyResult.Success -> {
                            Log.d("SearchLocationFragment", "result: ${results.resultData}")
                            setStartMapPoint(kakaoMap, results.resultData)
                            setPoiListOnMap(kakaoMap, results.resultData)
                            searchAdapter.submitList(results.resultData)
                        }

                        is TimelyResult.Loading -> {

                        }

                        is TimelyResult.NetworkError -> {

                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private val readyCallback: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            // 인증 후 API 가 정상적으로 실행될 때 호출됨
            Log.i("k3f", "startPosition: " + kakaoMap.cameraPosition!!.position.toString())
            Log.i("k3f", "startZoomLevel: " + kakaoMap.zoomLevel)

            getSearchLocationList(kakaoMap)
            infoWindowLayer = kakaoMap.mapWidgetManager!!.infoWindowLayer

//            kakaoMap.setOnInfoWindowClickListener { kakaoMap, infoWindow, guiId ->
//                val clickedPoi = searchViewModel.locationsList.value.let { result ->
//                    if (result is TimelyResult.Success) {
//                        result.resultData.find { it.noorLat == guiId }
//                    } else null
//                }
//                if (clickedPoi != null) {
//                    setNaviGraph(clickedPoi)
//                } else {
//                    toastShort(requireContext(), "선택한 장소 정보를 찾을 수 없습니다.")
//                }
//            }
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

    private fun setStartMapPoint(kakaoMap: KakaoMap, poiList: List<PoiItem>) {
        poiList.first().apply {
            placeStartPosition = LatLng.from(noorLat.toDouble(), noorLon.toDouble())
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(placeStartPosition)
            kakaoMap.moveCamera(cameraUpdate)
        }
    }

    private fun setPoiListOnMap(kakaoMap: KakaoMap, poiList: List<PoiItem>) {
        val labelManager = kakaoMap.labelManager
        poiList.forEach { poiItem ->
            val poiLocation = LatLng.from(poiItem.noorLat.toDouble(), poiItem.noorLon.toDouble())
            val labelStyle = LabelStyles.from(LabelStyle.from(R.drawable.blue_marker))
            val labelOptions = LabelOptions.from(poiLocation).setStyles(labelStyle)
            labelManager?.layer?.addLabel(labelOptions)

//            infoWindowLayer.addInfoWindow(getStateMessageLayout(
//                messageId = poiItem.noorLat,
//                placeName = poiItem.name,
//                location = poiLocation))
//            infoWindowLayer.getInfoWindow(poiItem.noorLat).show()
        }
    }
//    private fun getStateMessageLayout(messageId:String, placeName: String, location: LatLng): InfoWindowOptions {
//        val body = GuiLayout(Orientation.Horizontal)
//        body.setPadding(20, 20, 20, 18)
//
//        val image = GuiImage(R.drawable.window_body, true)
//        image.setFixedArea(7, 7, 7, 7)
//        body.setBackground(image)
//
//        val text = GuiText(placeName)
//        text.setTextSize(30)
//        body.addView(text)
//
//        val options = InfoWindowOptions.from(
//            messageId,
//            location
//        )
//        options.setBody(body)
//        options.setBodyOffset(0f, -4f)
//        options.setTail(GuiImage(R.drawable.window_tail, false))
//        options.setTailOffset(0f,-50f)
//        options.setVisible(false)
//        return options
//    }

    private fun goGroupAddSchedule(poiItem: PoiItem) {
        val action =
            SearchLocationFragmentDirections.actionSearchLocationFragmentToGroupAddScheduleFragment(
                poiItem.noorLat,
                poiItem.noorLon,
                poiItem.name,
                args.groupId
            )
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.groupAddScheduleFragment, inclusive = true)
            .build()
        findNavController().navigate(action, navOptions)
    }

    private fun goAddSchedule(poiItem: PoiItem) {
        val action =
            SearchLocationFragmentDirections.actionSearchLocationFragmentToAddScheduleFragment(
                poiItem.noorLat,
                poiItem.noorLon,
                poiItem.name
            )
        val navOptions = NavOptions.Builder()
            .setPopUpTo(
                R.id.addScheduleFragment,
                inclusive = true
            ) // Ensure old instance is removed
            .build()
        findNavController().navigate(action, navOptions)
    }

    override fun onResume() {
        super.onResume()
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}
