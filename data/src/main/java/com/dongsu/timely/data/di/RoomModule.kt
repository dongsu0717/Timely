package com.dongsu.timely.data.di

import android.content.Context
import androidx.room.Room
import com.dongsu.timely.data.local.room.dao.ScheduleDAO
import com.dongsu.timely.data.local.room.TimelyRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TimelyRoomDatabase =
        TimelyRoomDatabase.getDatabase(context)
}