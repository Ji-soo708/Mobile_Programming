package com.example.kulendar.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MYDBHelper_Subject(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME="subject.db"
        val DB_VERSION=1
        val TABLE_NAME="Subject"
        val SubName="SubName"
        val SubNum="SubNum"
        val SubTime="SubTime"
        val SubProf="SubProf"
        val SubScore="SubScore"
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
                "$SubNum text primary key,"+
                "$SubName text,"+
                "$SubTime text,"+
                "$SubScore text,"+
                "$SubProf text);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }
}