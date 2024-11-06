package com.dongsu.timely.data.di

import com.dongsu.timely.data.local.room.TimelyRoomDatabase
import com.dongsu.timely.data.local.room.dao.ScheduleDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DAOModule {
    @Provides
    fun provideScheduleDao(
        database: TimelyRoomDatabase
    ): ScheduleDAO =
        database.scheduleDao()
}