//package com.dongsu.timely.service.fcm
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import androidx.core.app.NotificationCompat
//import androidx.core.content.ContextCompat.getSystemService
//import com.dongsu.service.R
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//
//
///*
//id
//각 알림 채널을 식별하는 고유한 문자열
//알림을 생성할 때 이 채널 ID를 사용하여 알림이 특정 채널로 전송됩니다. 즉, 알림을 받을 때 사용자가 어떤 채널에서 오는 알림인지 구분
//8.0 (API 레벨 26) 이상에서는 알림을 보내기 위해 반드시 알림 채널을 설정해야 합니다. 이 채널 ID가 없으면 알림이 표시되지 않습니다.
//
//name
//사용자가 확인할 수 있는 채널의 이름
//알림 채널 설정에서 사용자가 채널을 이해할 수 있도록 설명하는 역할을 합니다. 예를 들어, PUSH_SESAC라는 이름은 이 채널이 무엇과 관련된 것인지 알 수 있게 해줌
//사용자는 기기 설정에서 알림 채널을 관리할 수 있으며, 이 이름을 통해 각 채널의 용도를 쉽게 파악할 수 있습니다. 사용자는 필요에 따라 특정 채널의 알림을 끄거나 조정할 수 있다
//
//1 여러 알림 채널을 생성하여 각 알림 유형을 관리합니다.
//2 FCM 메시지 수신 시, 메시지의 내용에 따라 적절한 채널 ID를 선택하여 알림을 전송합니다.
//3 알림 전송 메소드에서 채널 ID를 사용하여 알림을 표시합니다.
//
//*/
//
//const val GROUP_CHANNEL_ID = "GROUP_CHANNEL_ID"
//const val GROUP_CHANNEL_NAME = "Group Notifications"
//const val APPOINTMENT_CHANNEL_ID = "APPOINTMENT_CHANNEL_ID"
//const val APPOINTMENT_CHANNEL_NAME = "Appointment Notifications"
//const val TAG = "FCM_TAG"
//
//class MyFirebaseMessagingService : FirebaseMessagingService() {
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onMessageReceived(pushMessage: RemoteMessage) {
//        super.onMessageReceived(pushMessage)
//        Log.e("ㅇㄴㅇ", "pushMessage: $pushMessage, id: ${pushMessage.messageId}")
//
//        val pushTitle = pushMessage.notification?.title
//        val pushBody = pushMessage.notification?.body
//        val groupId = pushMessage.data["groupId"]
//        val userId = pushMessage.data["userId"]
//        val channelId = pushMessage.data["channelId"]
//        Log.e(
//            TAG, "groupId: $groupId, userId: $userId, channel: $channelId\n"
//                    + "rawData: ${pushMessage.rawData}\n"
//                    + "data: ${pushMessage.data}\n"
//                    + "title: $pushTitle\n"
//                    + "body: $pushBody"
//        )
//        /*
//        channelId = CREATE_SCHEDULE
//        channeld = APPOINTMENT+ALARM
//         */
//
//        sendToStatusBarPushMessage(pushTitle, pushBody, groupId, userId)
//
//        // 알림 유형에 따라 적절한 채널 ID 선택
////        val channelId = when {
////
////            pushBody?.contains("가입") == true -> GROUP_CHANNEL_ID // 그룹 가입 알림
////            pushBody?.contains("약속") == true -> APPOINTMENT_CHANNEL_ID // 약속 알림
////            else -> GROUP_CHANNEL_ID // 기본 채널 선택
////        }
//
////        if (pushTitle != null && pushBody != null) {
////            sendToStatusBarPushMessage(pushTitle, pushBody, channelId, groupId, userId)
////        } else {
////            Log.e(TAG, "FCM 이 Null 로 왔네요")
////        }
//    }
//
//    private var notificationID = 1234
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun sendToStatusBarPushMessage(
//        title: String?,
//        body: String?,
//        groupId: String?,
//        userId: String?,
//    ) {
////        val pendingTarget = if (channelId == APPOINTMENT_CHANNEL_ID) {
////            Intent(this, MainActivity2::class.java) // APPOINTMENT_CHANNEL_ID일 때 MainActivity2로 전환
////        } else {
////            Intent(this, MainActivity::class.java) // 기본적으로 MainActivity로 전환
////        } // 누르면 여기로 가라
//        val pendingTarget = Intent(this, MainActivity2::class.java)
//
//        //그룹 아이디 받아서 해당 그룹으로 보내기
//        /*
//        val pendingTarget = if (groupId != null) {
//        // groupId가 있을 경우, GroupActivity로 이동
//        Intent(this, GroupActivity::class.java).apply {
//            putExtra("groupId", groupId)
//        }
//    } else {
//        // groupId가 없으면 기본 MainActivity로 이동
//        Intent(this, MainActivity::class.java)
//    }
//         */
//
//        //그룹액티비티 부분
//        /*
//         // Intent로부터 groupId를 받아 처리
//        val groupId = intent.getStringExtra("groupId")
//        if (groupId != null) {
//            // groupId를 사용해 서버나 DB에서 그룹 정보를 가져와 표시
//            displayGroupDetails(groupId)
//        } else {
//            // groupId가 없을 때 처리할 로직 (예: 에러 메시지 표시)
//            Log.e("GroupActivity", "groupId가 전달되지 않았습니다.")
//        }
//         */
//
//        // 전달할 추가 정보 설정
//        pendingTarget.putExtra("fcm_message", body)
//        groupId?.let { pendingTarget.putExtra("groupId", it) }
//        userId?.let { pendingTarget.putExtra("userId", it) }
//
//
//        val pendingIntent = PendingIntent.getActivity(
//            this, 100, pendingTarget,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT // 업데이트 플래그 추가
//        )
//        pendingTarget.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//        //알림 생성
//        val notificationBuilder = NotificationCompat.Builder(this, GROUP_CHANNEL_ID)
//            .setContentTitle(title ?: "Default Title")
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentText(body ?: "Default Body")
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true) // 알림 클릭 시 자동으로 제거
//
//        // 알림 매니저로 알림 표시
//        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // 그룹 채널 생성
//            val groupChannel = NotificationChannel(
//                GROUP_CHANNEL_ID,
//                GROUP_CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            nm.createNotificationChannel(groupChannel)
//
//            // 약속 채널 생성
////            val appointmentChannel = NotificationChannel(
////                APPOINTMENT_CHANNEL_ID,
////                APPOINTMENT_CHANNEL_NAME,
////                NotificationManager.IMPORTANCE_HIGH // 약속 알림은 중요도가 높을 수 있습니다.
////            )
////            nm.createNotificationChannel(appointmentChannel)
//        }
//
//        nm.notify(notificationID, notificationBuilder.build())
//    }
//
//    //새로운 Registration ID(Token)을 발급 받으면 호출
//    //등록 ID(Token) 가 변경되는 대표적 이유
//    override fun onNewToken(token: String) {
//        Log.d("FCM", "New token: $token")
//        // 서버에 새 토큰을 전송하거나 저장하는 로직
//    }
//}
//
///*
//const val CHANNEL_ID = "FCM_CHANNEL_ID"
//const val CHANNEL_NAME = "PUSH_SESAC"
//const val TAG = "FCM_TAG"
//
//class SeSACFCMService : FirebaseMessagingService() {
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onMessageReceived(pushMessage: RemoteMessage) {
//        super.onMessageReceived(pushMessage)
//
//        Log.e(TAG, "${pushMessage.rawData?.let { String(it) }}")
//        val pushTitle = pushMessage.notification?.title
//        val pushBody = pushMessage.notification?.body
//        if( pushTitle != null && pushBody != null){
//            sendToStatusBarPushMessage(pushTitle, pushBody)
//        }else{
//            Log.e(TAG, "FCM 이 Null 로 왔네요")
//        }
//    }
//
//    private var notificationID = 1234
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun sendToStatusBarPushMessage(title: String, body: String) {
//        val pendingTarget = Intent(this, FCMReceivedActivity::class.java) //누르면 여기로 가라
//        pendingTarget.putExtra("fcm_message", body)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 100, pendingTarget,
//            PendingIntent.FLAG_IMMUTABLE
//        )
//        pendingTarget.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        val notificationBuilder: NotificationCompat.Builder =
//            NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle(title)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentText(body)
//                .setContentIntent(pendingIntent)
//        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            nm.createNotificationChannel(channel)
//        }
//        nm.notify(notificationID, notificationBuilder.build())
//    }
//    override fun onNewToken(token: String) {
//        Log.e(TAG, "새롭게 발급 받은 ToKen = $token")
//    }
//}
// */