package com.dongsu.timely.service

const val LOADING_GET_LOCATION = "위치 정보를 가져오는 중입니다 "
const val ERROR_GET_LOCATION = "위치 정보를 가져오는데 실패했습니다."

//채널
const val PERSONAL_CHANNEL_ID = "기본"
const val PERSONAL_CHANNEL_NAME = "개인 일정 알람입니다"
const val PERSONAL_ROUTE_CHANNEL_ID = "거리 표시"
const val PERSONAL_ROUTE_CHANNEL_NAME = "개인 일정 거리 및 시간 표시 알람입니다"
const val PERSONAL_ROUTE_CHANNEL_DESCRIPTION = "현재 위치에서 약속시간까지 거리, 차로 도착하는 시간, 걸어서 도착하는 시간을 보여줍니다."

//알람
const val TIME_TO_SCHEDULE = "일정시간이 %d분 남았습니다"
const val TIME_TO_ROUTE_SCHEDULE = "거리: %s, 자동차: %s, 도보: %s"
const val ALARM_ID = 1
const val ALARM_ROUTE_ID = 2

const val TMAP_PACKAGE_NAME = "com.skt.tmap.ku"
const val TMAP_ROUTE_URL = "tmap://route?goalname=%s&goalx=%s&goaly=%s&startx=%s&starty=%s"
const val TMAP_MARKET_URL = "market://details?id=%s"