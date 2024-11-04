package com.dongsu.timely.data.di

import android.content.Context
import androidx.room.Room
import com.dongsu.timely.data.local.dao.ScheduleDAO
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
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TimelyRoomDatabase {
        return TimelyRoomDatabase.getDatabase(context)
    }

    @Provides
    fun provideScheduleDao(database: TimelyRoomDatabase): ScheduleDAO {
        return database.scheduleDao()
    }
}