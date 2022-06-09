package com.example.kulendar.DB

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kulendar.DB.MYDBHelper_User.Companion.User_email
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user:User)

    @Query("SELECT * FROM UserTable")
    fun getUsers():List<User>

    @Query("SELECT * FROM UserTable WHERE User_email = :email AND User_pw = :password")
    fun getUser(email:String, password:String) : User?







}
