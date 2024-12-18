package com.dongsu.timely.data.di

import com.dongsu.timely.data.repository.GroupRepositoryImpl
import com.dongsu.timely.data.repository.GroupScheduleRepositoryImpl
import com.dongsu.timely.data.repository.ScheduleRepositoryImpl
import com.dongsu.timely.data.repository.TMapRepositoryImpl
import com.dongsu.timely.data.repository.UserRepositoryImpl
import com.dongsu.timely.domain.repository.GroupRepository
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import com.dongsu.timely.domain.repository.ScheduleRepository
import com.dongsu.timely.domain.repository.TMapRepository
import com.dongsu.timely.domain.repository.UserRepository
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

    @Binds
    @Singleton
    abstract fun provideUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository

    @Binds
    @Singleton
    abstract fun provideGroupRepository(
        groupRepositoryImpl: GroupRepositoryImpl,
    ): GroupRepository

    @Binds
    @Singleton
    abstract fun provideGroupScheduleRepository(
        groupShedRepositoryImpl: GroupScheduleRepositoryImpl,
    ): GroupScheduleRepository

}

