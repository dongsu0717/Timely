package com.dongsu.timely.data.di

import com.dongsu.timely.data.remote.service.TmapService
import com.dongsu.timely.data.repository.LocationRepositoryImpl
import com.dongsu.timely.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideLocationRepository(
        tmapService: TmapService,
    ): LocationRepository = LocationRepositoryImpl(tmapService)
}
