package com.example.kulendar.DB


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.Time
import java.time.zone.ZoneOffsetTransitionRule

class MyDBHelper_TimeTable(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME="TimeTable.db"
        val DB_VERSION=1
        val TABLE_NAME="TimeTable"
        val TimeTable_id="TimetableId"
        val User_name="Username"
        val Sub_num="SubNum"

    }
    @SuppressLint("Range")
    fun getAllRecord(){
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        //showRecord(cursor)
        while(cursor.moveToNext()){
            var Username=cursor.getString(cursor.getColumnIndex("Username"))
            var SubNum=cursor.getString(cursor.getColumnIndex("SubNum"))
            Log.i("timetable",Username+"GAR")
            Log.i("timetable",SubNum+"GAR")


        }
        cursor.close()
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME("+
                "$TimeTable_id integer primary key autoincrement, "+
                "$User_name text ,"+
                "$Sub_num text );"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }
    fun findProduct(user:String,num:String):Boolean{
        val strsql="select $Sub_num from $TABLE_NAME where $User_name='$user' and $Sub_num='$num';"
        val db=readableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        cursor.close()
        db.close()
        return flag
    }
    @SuppressLint("Range")
    fun findTime(user:String):MutableList<String>?{
        val strsql="select $Sub_num from $TABLE_NAME where $User_name='$user';"
        val db=readableDatabase
        val cursor=db.rawQuery(strsql,null)
        var numlist=mutableListOf<String>()
        while(cursor.moveToNext()){
            var num=cursor.getString(cursor.getColumnIndex("SubNum"))
            numlist.add(num)
            Log.i("timetable",num)

        }

        Log.i("timetablenumlist",numlist.toString())
        return numlist
    }
    @SuppressLint("Range")
    fun deleteProduct(user:String, num:String):Boolean{
        val strsql="select $TimeTable_id from $TABLE_NAME where $User_name='$user' and $Sub_num='$num';"
        val db=writableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        var ttid:String?=null
        while(cursor.moveToNext()) {
            ttid = cursor.getString(cursor.getColumnIndex("TimetableId"))
        }
        if(flag){
            cursor.moveToFirst()
            db.delete(TABLE_NAME,"$TimeTable_id=?", arrayOf(ttid))
        }
        cursor.close()
        db.close()
        return flag

    }
    fun insertProduct(user:String,num:String):Boolean{
        val values= ContentValues()
        values.put(Sub_num,num)
        values.put(User_name,user)
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