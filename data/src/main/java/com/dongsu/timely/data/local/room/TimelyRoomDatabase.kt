package com.dongsu.timely.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dongsu.timely.data.local.room.dao.ScheduleDAO
import com.dongsu.timely.data.local.room.entity.ScheduleInfo

@Database(entities = [ScheduleInfo::class], version = 2, exportSchema = false)
abstract class TimelyRoomDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDAO

    companion object {
        private const val DB_NAME = "schedule_database"

        @Volatile
        private var instance: TimelyRoomDatabase? = null

        fun getDatabase(context: Context): TimelyRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): TimelyRoomDatabase {
            return Room.databaseBuilder(
                context,
                TimelyRoomDatabase::class.java, DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
