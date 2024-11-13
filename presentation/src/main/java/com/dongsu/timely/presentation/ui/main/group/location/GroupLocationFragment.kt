package com.dongsu.timely.presentation.ui.main.group.location

import android.graphics.Color
import android.util.Log
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentGroupLocationBinding
import com.dongsu.timely.presentation.common.BaseTabFragment
import com.dongsu.timely.presentation.common.CommonUtils
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.shape.Polygon

class GroupLocationFragment: BaseTabFragment<FragmentGroupLocationBinding>(FragmentGroupLocationBinding::inflate)  {

    private lateinit var mapView: MapView

    private val startZoomLevel = 15
    private val startPosition = LatLng.from(37.46819965686225, 126.90119500104446)
    private lateinit var circleWavePolygon: Polygon

    override fun initView() {
        Log.e("위치", groupId.toString())
        mapView = binding.mapView
        mapView.start(lifeCycleCallback,readyCallback)
    }
    private val readyCallback: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {

            // 인증 후 API 가 정상적으로 실행될 때 호출됨
            Log.i("k3f", "startPosition: " + kakaoMap.cameraPosition!!.position.toString())
            Log.i("k3f", "startZoomLevel: " + kakaoMap.zoomLevel)
            val labelManager = kakaoMap.labelManager
            val shapeManager = kakaoMap.shapeManager
            val routeLineManager = kakaoMap.routeLineManager
            val mapWidgetManager = kakaoMap.mapWidgetManager
            val trackingManager = kakaoMap.trackingManager
//            getCircleWaveAnimator(kakaoMap)


            //라벨 스타일 생성
            val styles = labelManager?.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(R.drawable.red_dot_marker).setTextStyles(32,
                Color.RED)))

            val myPos = LatLng.from(37.46819965686225, 126.90119500104446)

            // 2. LabelOptions 생성하기
            val options = LabelOptions.from(myPos).setStyles(styles)

            // 3. LabelLayer 가져오기 (또는 커스텀 Layer 생성)
            val layer = labelManager?.layer

            // 4. LabelLayer 에 LabelOptions 을 넣어 Label 생성하기
            val label: Label = layer!!.addLabel(options)


            // circle wave 를 위한 반지름 1 짜리 Polygon 을 생성한다.
//            circleWavePolygon = kakaoMap.shapeManager!!.layer.addPolygon(
//                PolygonOptions.from("circlePolygon")
//                    .setVisible(true)
//                    .setDotPoints(DotPoints.fromCircle(LatLng.from(37.46819965686, 126.90119500146), 1.0f))
//                    .setStylesSet(
//                        PolygonStylesSet.from(
//                            PolygonStyles.from(Color.RED)
//                        )
//                    )
//            )
//            val circleOptions =
//                PolygonOptions.from(DotPoints.fromCircle(LatLng.from(37.46819965686, 126.90119500146), 200f))
////                    .setVisible(false)
//                    .setStylesSet(
//                        PolygonStylesSet.from(
//                            PolygonStyles.from(
//                        R.color.green)))
//            val circle = shapeManager!!.layer.addPolygon(circleOptions)
//
//            //Label 의 transform 에 따라 움직이도록 설정한
////            label.addShareTransform(circle)
//            getCircleWaveAnimator(kakaoMap,shapeManager).stop()
//            getCircleWaveAnimator(kakaoMap,shapeManager).addPolygons(circleWavePolygon)
//            getCircleWaveAnimator(kakaoMap,shapeManager).start()

        }

        override fun getPosition(): LatLng {
            // 지도 시작 시 위치 좌표를 설정
            return startPosition
        }

        override fun getZoomLevel(): Int {
            // 지도 시작 시 확대/축소 줌 레벨 설정
            return startZoomLevel
        }
        //위 3개 필수

        override fun getMapViewInfo(): MapViewInfo {
            return super.getMapViewInfo()
//            MapViewInfo.from(MapType.NORMAL);
            //기본 값은 MapType.NORMAL(“map”). 스카이뷰 외 다른 것 사용은 문의 필요
            //mapStyle	기본 값은 “default”. 다른 스타일 사용은 문의 필요
        }

        override fun getViewName(): String {
            // KakaoMap 의 고유한 이름을 설정
            return "MyFirstMap"
        }

        override fun isVisible(): Boolean {
            // 지도 시작 시 visible 여부를 설정
            return true
        }

        override fun getTag(): String {
            // KakaoMap 의 tag 을 설정
            return "FirstMapTag"
        }

        override fun isDev(): Boolean {
            return super.isDev()
        }

        override fun getTimeout(): Int {
            return super.getTimeout()
        }

    }

    // MapLifeCycleCallback 을 통해 지도의 LifeCycle 관련 이벤트를 수신할 수 있다.
    private val lifeCycleCallback: MapLifeCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapResumed() {
            super.onMapResumed()
        }

        override fun onMapPaused() {
            super.onMapPaused()
        }

        override fun onMapDestroy() {
            CommonUtils.toastShort(requireContext(),"onMapDestroy")
            // 지도 API 가 정상적으로 종료될 때 호출됨
        }

        override fun onMapError(error: java.lang.Exception) {
            CommonUtils.toastShort(requireContext(),"onMapError")
            // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
        }
    }

}