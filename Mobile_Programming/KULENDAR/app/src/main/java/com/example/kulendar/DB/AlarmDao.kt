package com.example.kulendar.DB

import androidx.room.*

@Dao
interface AlarmDao {
    @Query("SELECT * FROM AlarmTable")
    fun getAll(): List<Alarm>

    @Query("SELECT * FROM AlarmTable WHERE notification_ID = :notification_ID")
    fun getAlarm(notification_ID:Int) : Alarm?

    @Insert
    fun insertAlarm(alarm:Alarm)

    @Update
    fun update(alarm: Alarm)

    @Delete
    fun deleteAlarm(alarm:Alarm)
}