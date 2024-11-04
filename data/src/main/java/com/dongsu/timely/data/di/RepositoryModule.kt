package com.dongsu.timely.data.di

import com.dongsu.timely.data.repository.LocationRepositoryImpl
import com.dongsu.timely.data.repository.ScheduleRepositoryImpl
import com.dongsu.timely.domain.repository.LocationRepository
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
    abstract fun provideLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl,
    ): LocationRepository
}

