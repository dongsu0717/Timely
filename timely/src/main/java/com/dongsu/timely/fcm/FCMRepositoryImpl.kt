package com.dongsu.timely.fcm

import android.util.Log
import com.dongsu.timely.domain.repository.FCMRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FCMRepositoryImpl @Inject constructor(
): FCMRepository {
    override suspend fun getFCMToken(): String {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Log.e("FCM토큰 발급 실패 ", task.toString(), task.exception)
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error"))
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.e("FCM토큰 발급 성공 ", token)
                continuation.resume(token)
            }
        }
    }
}