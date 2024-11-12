package com.dongsu.timely.service.di

import com.dongsu.timely.domain.repository.AlarmRepository
import com.dongsu.timely.domain.repository.FCMRepository
import com.dongsu.timely.service.alarm.AlarmRepositoryImpl
import com.dongsu.timely.service.fcm.FCMRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmModule {

    @Binds
    @Singleton
    abstract fun bindAlarmScheduler(
        alarmSchedulerImpl: AlarmRepositoryImpl
    ): AlarmRepository

    @Binds
    @Singleton
    abstract fun provideFCMRepository(
        fcmRepositoryImpl: FCMRepositoryImpl,
    ): FCMRepository
}