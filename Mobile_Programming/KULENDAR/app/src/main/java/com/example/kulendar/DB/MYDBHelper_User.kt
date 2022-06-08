package com.example.kulendar.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

///////////////////회원가입, 로그인 사용안함///////////////////////
class MYDBHelper_User(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME="user.db"
        val DB_VERSION=1
        val TABLE_NAME="User"
        val User_id="user_id"
        val User_pw="user_pw"
        val User_name="user_name"
        val User_department="user_department"
        val User_email="user_email"

    }

    fun insert(user: User):Boolean{
        val values = ContentValues()
        values.put(User_email, user.User_email)
        values.put(User_pw, user.User_pw)
        values.put(User_name, user.User_name)
        values.put(User_department, user.User_department)
        val db = writableDatabase
        val flag = db.insert(TABLE_NAME, null, values)>0
        db.close()
        return flag



    }
    fun getAllRecord(){
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        //showRecord(cursor)
        cursor.close()
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME("+
                "$User_id integer primary key autoincrement, "+
                "$User_email text, "+
                "$User_pw text, "+
                "$User_name text, "+
                "$User_department text);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }


}