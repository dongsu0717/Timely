package com.dongsu.timely.data.di

import com.dongsu.timely.data.datasource.remote.GroupRemoteDatasource
import com.dongsu.timely.data.datasource.remote.GroupScheduleRemoteDatasource
import com.dongsu.timely.data.datasource.local.ScheduleLocalDatasource
import com.dongsu.timely.data.datasource.local.UserLocalDatasource
import com.dongsu.timely.data.datasource.remote.UserRemoteDatasource
import com.dongsu.timely.data.datasource.local.ScheduleLocalDatasourceImpl
import com.dongsu.timely.data.datasource.local.UserLocalDatasourceImpl
import com.dongsu.timely.data.datasource.remote.GroupRemoteDatasourceImpl
import com.dongsu.timely.data.datasource.remote.GroupScheduleRemoteDatasourceImpl
import com.dongsu.timely.data.datasource.remote.UserRemoteDatasourceImpl
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