package com.dongsu.timely.data.di

import com.dongsu.timely.data.mapper.GroupMapper
import com.dongsu.timely.data.mapper.GroupMeetingInfoMapper
import com.dongsu.timely.data.mapper.GroupScheduleMapper
import com.dongsu.timely.data.mapper.InviteCodeMapper
import com.dongsu.timely.data.mapper.LocationMapper
import com.dongsu.timely.data.mapper.PoiLocationMapper
import com.dongsu.timely.data.mapper.ScheduleMapper
import com.dongsu.timely.data.mapper.TargetLocationMapper
import com.dongsu.timely.data.mapper.UserMapper
import com.dongsu.timely.data.mapper.UserMeetingMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    @Singleton
    fun providePoiLocationMapper(): PoiLocationMapper =
        PoiLocationMapper()

    @Provides
    @Singleton
    fun provideScheduleMapper(): ScheduleMapper =
        ScheduleMapper()

    @Provides
    @Singleton
    fun provideUserMapper(): UserMapper =
        UserMapper()

    @Provides
    @Singleton
    fun userMeetingMapper(
        userMapper: UserMapper,
        locationMapper: LocationMapper
    ): UserMeetingMapper =
        UserMeetingMapper(userMapper, locationMapper)

    @Provides
    @Singleton
    fun provideLocationMapper(): LocationMapper =
        LocationMapper()


    @Provides
    @Singleton
    fun provideGroupMapper(
        userMapper: UserMapper
    ): GroupMapper =
        GroupMapper(userMapper)

    @Provides
    @Singleton
    fun groupScheduleMapper(): GroupScheduleMapper =
        GroupScheduleMapper()

    @Provides
    @Singleton
    fun provideTargetLocationMapper(
        locationMapper: LocationMapper
    ): TargetLocationMapper =
        TargetLocationMapper(locationMapper)

    @Provides
    @Singleton
    fun provideGroupMeetingInfoMapper(
        targetLocationMapper: TargetLocationMapper,
        userMeetingMapper: UserMeetingMapper,
    ): GroupMeetingInfoMapper =
        GroupMeetingInfoMapper(targetLocationMapper, userMeetingMapper)

    @Provides
    @Singleton
    fun provideInviterMapper(): InviteCodeMapper =
        InviteCodeMapper()

}