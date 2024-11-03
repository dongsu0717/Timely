package com.dongsu.timely.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dongsu.timely.data.local.dao.ScheduleDAO
import com.dongsu.timely.data.local.entity.ScheduleInfo

@Database(entities = [ScheduleInfo::class], version = 1, exportSchema = false)
abstract class TimelyRoomDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDAO

    companion object{
        private const val DB_NAME = "schedule_database"
        /**
         * @Volatile 을 변수에 선언하면 해당 변수는 메인 메모리에만 저장
         * 멀티 쓰레드 환경에서 메인 메모리의 값을 참조하므로 변수 값 불일치 문제를 해결
         * 수행성능은 떨어 질 수 있다
         */
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

//    companion object {
//        private lateinit var INSTANCE: TimelyRoomDatabase
//         fun getDatabase(context: Context): TimelyRoomDatabase {
//            if (!this::INSTANCE.isInitialized) {
//                synchronized(TimelyRoomDatabase::class.java) {
//                    INSTANCE =
//                        Room.databaseBuilder(
//                            context.applicationContext,
//                            TimelyRoomDatabase::class.java,
//                            "schedule_database"
//                        )
//                            .fallbackToDestructiveMigration() //스키마 파괴하고 다시 만들기(테스트용)
//                            .build()
//                }
//            }
//            return INSTANCE
//        }
//
//    }
}