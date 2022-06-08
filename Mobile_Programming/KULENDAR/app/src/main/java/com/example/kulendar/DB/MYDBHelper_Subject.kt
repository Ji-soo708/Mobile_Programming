package com.example.kulendar.DB

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

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
    @SuppressLint("Range")
    fun getAllRecord(){
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        //showRecord(cursor)
        while(cursor.moveToNext()){
            var SubNum=cursor.getString(cursor.getColumnIndex("SubNum"))
            var SubName=cursor.getString(cursor.getColumnIndex("SubName"))
            var SubTime=cursor.getString(cursor.getColumnIndex("SubTime"))
            var SubProf=cursor.getString(cursor.getColumnIndex("SubProf"))
            var SubScore=cursor.getString(cursor.getColumnIndex("SubScore"))
            Log.i("db",SubNum)
            Log.i("db",SubName)
            Log.i("db",SubTime)
            Log.i("db",SubProf)
            Log.i("db",SubScore)
        }
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
    fun findProduct(num:String):Boolean{
        val strsql="select * from $TABLE_NAME where $SubNum='$num';"
        val db=readableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        cursor.close()
        db.close()
        return flag
    }
    @SuppressLint("Range")
    fun findTime(num:String): String? {
        val strsql="select $SubTime from $TABLE_NAME where $SubNum='$num';"
        val db=readableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        var timeinfo:String?=null
        if(flag) {
            while (cursor.moveToNext()) {
                timeinfo = cursor.getString(cursor.getColumnIndex("SubTime"))
            }
        }
        else
            timeinfo="nodata"
        cursor.close()
        db.close()
        return timeinfo
    }
    @SuppressLint("Range")
    fun findName(num:String): String {
        val strsql="select $SubName from $TABLE_NAME where $SubNum='$num';"
        val db=readableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        var nameinfo:String=""
        if(flag) {
            while (cursor.moveToNext()) {
                nameinfo = cursor.getString(cursor.getColumnIndex("SubName"))
            }
        }
        else {
            Log.i("findname", "데이터없음")
        }
        cursor.close()
        db.close()

        return nameinfo
    }
    fun insertProduct(subject:Subject):Boolean{
        val values= ContentValues()
        values.put(SubName,subject.SubName)
        values.put(SubNum,subject.SubNum)
        values.put(SubProf,subject.Professor)
        values.put(SubTime,subject.SubTime)
        values.put(SubScore,subject.Score)
        val db=writableDatabase
        if(db.insert(TABLE_NAME,null,values)>0){
            db.close()
            return true
        }
        else {
            db.close()
            return false
        }
    }
}