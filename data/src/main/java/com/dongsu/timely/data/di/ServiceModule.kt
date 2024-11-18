package com.dongsu.timely.data.di

import com.dongsu.timely.data.remote.api.FCMService
import com.dongsu.timely.data.remote.api.GroupScheduleService
import com.dongsu.timely.data.remote.api.GroupService
import com.dongsu.timely.data.remote.api.LoginService
import com.dongsu.timely.data.remote.api.TMapService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideTMapRepository(
        @TMap retrofit: Retrofit,
    ): TMapService = retrofit.create(TMapService::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(
        @Timely retrofit: Retrofit,
    ): LoginService = retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(
        @TimelyAuth retrofit: Retrofit,
    ): GroupService = retrofit.create(GroupService::class.java)

    @Provides
    @Singleton
    fun provideGroupScheduleRepository(
        @TimelyAuth retrofit: Retrofit,
    ): GroupScheduleService = retrofit.create(GroupScheduleService::class.java)

    @Provides
    @Singleton
    fun provideFCMRepository(
        @TimelyAuth retrofit: Retrofit,
    ): FCMService = retrofit.create(FCMService::class.java)
}


