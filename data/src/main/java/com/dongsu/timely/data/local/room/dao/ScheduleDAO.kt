package com.dongsu.timely.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.dongsu.timely.data.local.room.entity.ScheduleInfo

@Dao
interface ScheduleDAO {

    @Transaction
    @Insert
    suspend fun insertSchedule(schedule: ScheduleInfo)

    @Query("SELECT * FROM schedule")
    suspend fun loadAllSchedule(): List<ScheduleInfo>
//
//    @Update
//    fun updateSchedule(schedule: ScheduleInfo)
//
//    @Query("DELETE FROM schedule WHERE scheduleId = :id")
//    fun deleteSchedule(id: Int)
//
//    //id값으로 스케줄 가져오기(수정페이지)
//    @Query("SELECT * FROM schedule WHERE scheduleId = :id")
//    fun getSchedule(id: Int): ScheduleInfo
//
//    @Query("SELECT * FROM schedule WHERE appointmentAlarm = 1")
//    suspend fun getSchedulesWithAlarm(): MutableList<ScheduleInfo>
//
//    //해당 날짜 스케줄 가져오기
//    @Query("SELECT * FROM schedule WHERE startDate = :date")
//    fun getScheduleDate(date: String): MutableList<ScheduleInfo>
//
//    //해당 월 스케줄 가져오기
//    @Query("SELECT * FROM schedule WHERE startDate LIKE :month || '%'")
//    fun getScheduleMonth(month: String): MutableList<ScheduleInfo>
}