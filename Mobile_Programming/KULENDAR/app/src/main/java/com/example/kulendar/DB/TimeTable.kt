package com.example.kulendar.DB

import androidx.room.PrimaryKey

data class TimeTable(var User_name:String,var Sub_num:String)
{
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
