package com.dongsu.timely.data.di

import com.dongsu.timely.data.repository.TMapRepositoryImpl
import com.dongsu.timely.data.repository.ScheduleRepositoryImpl
import com.dongsu.timely.domain.repository.TMapRepository
import com.dongsu.timely.domain.repository.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideScheduleRepository(
        scheduleRepositoryImpl: ScheduleRepositoryImpl,
    ): ScheduleRepository

    @Binds
    @Singleton
    abstract fun provideTMapRepository(
        locationRepositoryImpl: TMapRepositoryImpl,
    ): TMapRepository
}

