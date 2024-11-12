package com.dongsu.timely.data.di

import com.dongsu.timely.data.remote.api.GroupScheduleService
import com.dongsu.timely.data.remote.api.GroupService
import com.dongsu.timely.data.remote.api.LoginService
import com.dongsu.timely.data.remote.api.TmapService
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
    ): TmapService = retrofit.create(TmapService::class.java)

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
}


