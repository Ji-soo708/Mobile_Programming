package com.example.kulendar.DB

import androidx.room.*

@Dao
interface AlarmDao {
    @Query("SELECT * FROM AlarmTable WHERE email = :email")
    fun getAll(email:String): List<Alarm>

    @Insert
    fun insertAlarm(alarm:Alarm)

    @Update
    fun update(alarm: Alarm)

    @Delete
    fun deleteAlarm(alarm:Alarm)
}