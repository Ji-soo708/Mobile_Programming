package com.example.kulendar.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.kuleander.calendar.MyCalendarAdapter
import com.example.kuleander.calendar.MyCalendarData
import com.example.kulendar.dday.MyDdayData
import java.text.SimpleDateFormat
import java.util.*

class MYDBHelper_Schedule(val context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val data:ArrayList<MyCalendarData> = ArrayList()
    companion object{
        val DB_NAME="schedule.db"
        val DB_VERSION=1
        val TABLE_NAME="Schedule"
        val User_id="user_id"
        val PID="pid"
        val Sch_time="sch_time"
        val Sch_content="sch_content"

    }
    fun getAllRecord():ArrayList<MyCalendarData>{
        data.clear()
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        //showRecord(cursor)
        data.clear()
        cursor.moveToFirst()
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                var pid=cursor.getInt(1).toString()
                var Fdate=cursor.getString(2).toLong()
                var str=cursor.getString(3)
                val tDate = Date(Fdate)
                val formatYear = SimpleDateFormat("yyyy")
                val formatMonth = SimpleDateFormat("M")
                val formatDay = SimpleDateFormat("d")
                var sYear=formatYear.format(tDate).toInt()
                var sMonth=formatMonth.format(tDate).toInt()
                var sDay=formatDay.format(tDate).toInt()
                data.add(MyCalendarData(sYear,sMonth,sDay,str,pid))
            }
        }
        cursor.close()
        db.close()
        return data
    }

    fun getDateRecord(gYear:Int,gMonth:Int,gDay:Int):ArrayList<MyCalendarData>{
        data.clear()
//        val strsql="select * from $TABLE_NAME where $Sch_time='$sch_time';"   //이게 문제임
        val strsql="select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        //showRecord(cursor)
        cursor.moveToFirst()
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                var userid=cursor.getInt(0)
                var pid=cursor.getInt(1).toString()
                var str=cursor.getString(3)
                var date = cursor.getString(2)
                if(date.equals(gYear.toString()+gMonth.toString()+gDay.toString())) {
//                    if("$User_id"==userid.toString())
                    data.add(MyCalendarData(userid, gMonth, gDay, str, pid))
                }
            }
        }
        cursor.close()
        db.close()
        return data
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME("+
                "$User_id INTEGER, " +
                "$PID INTEGER primary key AUTOINCREMENT,"+
                "$Sch_time text, "+
                "$Sch_content text);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }

    fun findProduct(sch: Date): Boolean {
        val strsql="select * from $TABLE_NAME where $Sch_time='$sch';"
        val db = readableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag = cursor.count!=0
        cursor.close()
        db.close()
        return flag
    }

    fun insertProduct(product: Schedule):Boolean{
        val values = ContentValues()
        values.put(User_id,product.User_id)
        values.put(Sch_time,product.Sch_time)
        values.put(Sch_content,product.Sch_content)
        val db=writableDatabase
        if(db.insert(TABLE_NAME,null,values)>0){
            db.close()
            return true
        }else{
            db.close()
            return false
        }
    }

    fun deleteProduct(pid: String): Boolean {
        val Ipid=pid.toInt()
        val strsql="select * from $TABLE_NAME where $PID='$Ipid';"
        val db = writableDatabase
        val cursor=db.rawQuery(strsql,null)
        val flag = cursor.count!=0
        if(flag){
            cursor.moveToFirst()
            db.delete(TABLE_NAME,"$PID=?", arrayOf(pid))
            //data.remove()
        }
        cursor.close()
        db.close()
        return flag
    }
}