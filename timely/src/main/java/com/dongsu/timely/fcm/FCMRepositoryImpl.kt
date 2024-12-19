package com.dongsu.timely.fcm

import com.dongsu.timely.domain.repository.FCMRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber
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
                    Timber.e(task.exception, task.toString())
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error"))
                    return@addOnCompleteListener
                }
                val token = task.result
                Timber.e(token)
                continuation.resume(token)
            }
        }
    }
}