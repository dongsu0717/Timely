package com.dongsu.timely.data.di

import com.dongsu.timely.data.repository.ScheduleRepositoryImpl
import com.dongsu.timely.domain.repository.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class RepositoryModule {
//
//    @Binds
//    @Singleton
//    abstract fun bindScheduleRepository(
//        scheduleRepositoryImpl: ScheduleRepositoryImpl
//    ): ScheduleRepository
//}

//@Module
//@InstallIn(SingletonComponent::class)
//interface RepositoryModule {
//
//    @Binds
//    @Singleton
//    fun provideScheduleRepository(scheduleRepositoryImpl: ScheduleRepositoryImpl): ScheduleRepository
//}

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideScheduleRepository(scheduleRepositoryImpl: ScheduleRepositoryImpl): ScheduleRepository {
        return scheduleRepositoryImpl
    }
}

