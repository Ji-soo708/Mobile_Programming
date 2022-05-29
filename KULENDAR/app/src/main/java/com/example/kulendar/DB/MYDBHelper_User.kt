package com.example.kulendar.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
        val User_timetable="user_timetable"

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
                "$User_id text primary key, "+
                "$User_pw text, "+
                "$User_name text, "+
                "$User_department text, "+
                "$User_email text, "+
                "$User_timetable text);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }
}