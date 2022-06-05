package com.example.kulendar.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user:User)

    @Query("SELECT * FROM UserTable")
    fun getUsers():List<User>

    @Query("SELECT * FROM UserTable WHERE User_email = :email AND User_pw = :password")
    fun getUser(email:String, password:String) : User?

}