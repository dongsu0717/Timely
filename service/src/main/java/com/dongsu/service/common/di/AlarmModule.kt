package com.dongsu.service.common.di

import com.dongsu.service.common.AlarmRepositoryImpl
import com.dongsu.timely.domain.repository.AlarmRepository
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

}