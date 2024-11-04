package com.dongsu.timely.data.di

import com.dongsu.timely.data.remote.service.TmapService
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
    fun provideLocationRepository(
        retrofit: Retrofit,
    ) :TmapService = retrofit.create(TmapService::class.java)
}
