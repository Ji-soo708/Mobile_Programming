package com.example.kulendar.DB

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "UserTable")
data class User(
    var User_email:String,
    var User_pw:String,
    var User_name:String,
    var User_department:String
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
