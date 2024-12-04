package com.dongsu.timely.presentation.common

//Dialog - 로그인
const val LOGIN_TITLE = "로그인이 필요합니다"
const val LOGIN_MESSAGE = "카카오톡으로 로그인 하시겠습니까?"
const val LOGIN_POSITIVE_BUTTON = "카카오톡 로그인"
const val LOGIN_NEGATIVE_BUTTON = "취소"

//Dialog - 위치 활성화
const val LOCATION_SERVICE_ACTIVATION_TITLE = "위치 서비스 활성화 필요"
const val LOCATION_SERVICE_ACTIVATION_MESSAGE = "위치 서비스를 활성화해야 이 기능을 사용할 수 있습니다. 설정으로 이동하시겠습니까?"
const val LOCATION_SERVICE_ACTIVATION_POSITIVE_BUTTON = "설정으로 이동"
const val LOCATION_SERVICE_ACTIVATION_TAG = "LocationServiceDialog"

//Dialog - 위치 권한 필요성 설명
const val LOCATION_PERMISSION_NEEDED_TITLE = "위치 권한 필요"
const val LOCATION_PERMISSION_NEEDED_MESSAGE = "이 기능을 사용하려면 위치 권한이 필요합니다. 권한을 허용해 주세요."
const val LOCATION_PERMISSION_NEEDED_TAG = "LocationPermissionsNeededDialog"

//Dialog - 위치 권한 거부
const val LOCATION_PERMISSION_DENIED_TITLE = "위치 권한 거부"
const val LOCATION_PERMISSION_DENIED_MESSAGE = "위치 권한이 완전히 거부되었습니다. 이 기능을 사용하려면 설정에서 권한을 수동으로 허용해야 합니다."
const val LOCATION_PERMISSION_DENIED_POSITIVE_BUTTON = "설정으로 이동"
const val LOCATION_PERMISSION_DENIED_TAG = "LocationPermissionsDeniedDialog"

//LOG 확인 TAG
const val KAKAO_TAG = "KakaoLoginManager"

//캘린더
const val LOAD_SCHEDULE_LOADING = "일정을 불러오는 중입니다."
const val LOAD_SCHEDULE_SUCCESS = "일정을 불러왔습니다."
const val LOAD_SCHEDULE_EMPTY = "일정이 없습니다."
const val LOAD_SCHEDULE_ERROR = "일정을 불러오는데 실패하였습니다."

//스케줄 저장
const val SAVE_SUCCESS = "저장였습니다."
const val SAVE_ERROR = "저장에 실패하였습니다."
const val SAVE_LOADING = "저장중..."
const val LOADING = "로딩중..."
const val OMG = "니가왜떠???"

//가져오기
const val GET_ERROR = "데이터를 가져오는데 실패하였습니다."
const val GET_LOADING = "가져오는중..."
const val GET_EMPTY = "아무것도 없군요!"

//그룹 - 리스트
const val GET_GROUP_LIST_EMPTY = "그룹을 만들어 주세요!"
const val GET_GROUP_LIST_ERROR = "그룹 리스트를 가져오는데 실패하였습니다."

//tab
const val GROUP_SCHEDULE = "일정"
const val GROUP_LOCATION = "위치"
const val GROUP_MANAGEMENT = "관리"

//groupSchedule
const val DEFAULT_ALARM_TIME = 1

//GroupMeeting
const val DISTANCE_TO_ARRIVE_POINT = 100
const val DEFAULT_START_ZOOM_LEVEL = 15
const val SUCCESS_SEND_STATE_MESSAGE = "모두에게 상태메세지 전송 완료!"
const val FAIL_SEND_STATE_MESSAGE = "상태메세지 전송 실패"

//inviteGroup
const val INVITE_GROUP_MESSAGE = """Timely로 초대합니다. 
    개인 및 일정 관리의 최고의 어플!!. """
const val INVITE_CODE = "inviteCode"