package com.dongsu.timely.common

//TMap 연결
const val TMAP_PACKAGE_NAME = "com.skt.tmap.ku"
const val TMAP_ROUTE_URL = "tmap://route?goalname=%s&goalx=%s&goaly=%s&startx=%s&starty=%s"
const val MARKET_URL = "market://details?id=%s"

//기본 스케줄 채널
const val PERSONAL_CHANNEL_ID = "기본"
const val PERSONAL_CHANNEL_NAME = "개인 일정 알람입니다"
const val PERSONAL_ROUTE_CHANNEL_ID = "거리 표시"
const val PERSONAL_ROUTE_CHANNEL_NAME = "개인 일정 거리 및 시간 표시 알람입니다"
const val PERSONAL_ROUTE_CHANNEL_DESCRIPTION = "현재 위치에서 약속시간까지 거리, 차로 도착하는 시간, 걸어서 도착하는 시간을 보여줍니다."

//기본 스케줄 알람
const val TIME_TO_SCHEDULE = "일정시간이 %d분 남았습니다"
const val TIME_TO_ROUTE_SCHEDULE = "거리: %s, 자동차: %s, 도보: %s"

//FCM 채널
const val FCM_CREATE_SCHEDULE_CHANNEL_ID = "SCHEDULE_CREATE"
const val FCM_CREATE_SCHEDULE_CHANNEL_NAME = "SCHEDULE_CREATE"
const val FCM_APPOINTMENT_CHANNEL_ID = "SCHEDULE_BEFORE_ALARM"
const val FCM_APPOINTMENT_CHANNEL_NAME = "SCHEDULE_BEFORE_ALARM"

//FCM 알람
const val FCM_CREATE_SCHEDULE_BODY = "'%s' 스케줄이 추가되었습니다."
const val FCM_APPOINTMENT_TITLE = "'%s' 스케줄 1시간전 입니다."
const val FCM_APPOINTMENT_BODY = "약속시간 30분전부터 위치 공유가 시작됩니다. "

//Foreground Service 채널
const val FOREGROUND_SERVICE_CHANNEL_ID = "FOREGROUND_SERVICE"
const val FOREGROUND_SERVICE_CHANNEL_NAME = "FOREGROUND_SERVICE"

//Foreground Service 알람
const val FOREGROUND_SERVICE_BODY = "'%s'스케줄의 위치 공유가 시작됩니다. "


//전체 알림 ID
const val NOTIFICATION_SCHEDULE_ID = 100
const val NOTIFICATION_ROUTE_ID = 101
const val NOTIFICATION_FCM_CREATE_SCHEDULE_ID = 200
const val NOTIFICATION_FCM_APPOINTMENT_ALARM_ID = 201
const val NOTIFICATION_FOREGROUND_LOCATION_ID = 300

