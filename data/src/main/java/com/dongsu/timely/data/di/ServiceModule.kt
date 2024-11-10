package com.dongsu.timely.data.di

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
    @TMap
    @Singleton
    fun provideTMapRepository(
        @TMap retrofit: Retrofit,
    ) :TmapService = retrofit.create(TmapService::class.java)

    @Provides
    @Timely
    @Singleton
    fun provideUserRepository(
        @Timely retrofit: Retrofit,
    ) : LoginService = retrofit.create(LoginService::class.java)
}


