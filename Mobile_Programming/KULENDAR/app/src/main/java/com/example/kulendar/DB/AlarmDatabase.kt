package com.example.kulendar.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao() : AlarmDao

    companion object {
        private var instance: AlarmDatabase? = null

        @Synchronized
        fun getInstance(context: Context):AlarmDatabase? {
            if(instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "=Alarm_Database"
                ).allowMainThreadQueries().build()
            }
            return instance
        }
    }
}