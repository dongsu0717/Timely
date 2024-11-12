package com.dongsu.timely.data.di

import com.dongsu.timely.data.datasource.GroupRemoteDatasource
import com.dongsu.timely.data.datasource.GroupScheduleRemoteDatasource
import com.dongsu.timely.data.datasource.ScheduleLocalDatasource
import com.dongsu.timely.data.datasource.UserLocalDatasource
import com.dongsu.timely.data.datasource.UserRemoteDatasource
import com.dongsu.timely.data.datasource.datasourceImpl.local.ScheduleLocalDatasourceImpl
import com.dongsu.timely.data.datasource.datasourceImpl.local.UserLocalDatasourceImpl
import com.dongsu.timely.data.datasource.datasourceImpl.remote.GroupRemoteDatasourceImpl
import com.dongsu.timely.data.datasource.datasourceImpl.remote.GroupScheduleRemoteDatasourceImpl
import com.dongsu.timely.data.datasource.datasourceImpl.remote.UserRemoteDatasourceImpl
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

    @Binds
    @Singleton
    abstract fun provideUserRemoteDatasource(
        userRemoteDatasourceImpl: UserRemoteDatasourceImpl
    ): UserRemoteDatasource

    @Binds
    @Singleton
    abstract fun provideUserLocalDatasource(
        userLocalDatasourceImpl: UserLocalDatasourceImpl
    ): UserLocalDatasource

    @Binds
    @Singleton
    abstract fun provideGroupRemoteDatasource(
        groupRemoteDatasourceImpl: GroupRemoteDatasourceImpl
    ): GroupRemoteDatasource

    @Binds
    @Singleton
    abstract fun provideGroupScheduleRemoteDatasource(
        groupScheduleRemoteDatasourceImpl: GroupScheduleRemoteDatasourceImpl
    ): GroupScheduleRemoteDatasource

}