package com.dongsu.timely.data.di

import com.dongsu.timely.data.datasource.ScheduleLocalDatasource
import com.dongsu.timely.data.datasource.datasourceImpl.ScheduleLocalDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceModule {
    @Binds
    @Singleton
    abstract fun provideScheduleLocalDatasource(
        scheduleLocalDatasourceImpl: ScheduleLocalDatasourceImpl
    ): ScheduleLocalDatasource
}