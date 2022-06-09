package com.example.kulendar.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.kulendar.dday.MyDdayAdapter
import com.example.kulendar.dday.MyDdayData
import java.util.*

class MyDBHelper_Dday(val context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val data:ArrayList<MyDdayData> = ArrayList()
    private var d: Long = 0
    private var t: Long = 0
    private var r: Long = 0
    private var resultNumber = 0
    companion object{
        val DB_NAME="myddaydb.db"
        val DB_VERSION=1
        val TABLE_NAME="dday"
        val PID="pid"
        val User_id="user_id"
        val dYear="dYear"
        val dMonth="dMonth"
        val dDay="dDay"
        val Title="title"
    }

    fun getAllRecord(email:String):ArrayList<MyDdayData>{
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        //showRecord(cursor)
        data.clear()
        cursor.moveToFirst()
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                val calendar = Calendar.getInstance()
                val dCalendar = Calendar.getInstance()
                var userid=cursor.getString(0)
                var pid=cursor.getInt(1).toString()
                var dYear=cursor.getInt(2)
                var dMonth=cursor.getInt(3)
                var dDay=cursor.getInt(4)
                var str=cursor.getString(5)
                dCalendar[dYear, dMonth] = dDay
                t = calendar.timeInMillis
                d = dCalendar.timeInMillis
                r = (d - t) / (24 * 60 * 60 * 1000)
                resultNumber = r.toInt()
                if(email.equals(userid))
                    data.add(MyDdayData(resultNumber, str, pid))
            }
        }
        cursor.close()
        db.close()
        return data
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME("+
                "$User_id INTEGER, " +
                "$PID INTEGER primary key AUTOINCREMENT, "+
                "$dYear Int, "+
                "$dMonth Int," +
                "$dDay Int," +
                "$Title text);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }

    fun insertProduct(product: Dday):Boolean{
        val values = ContentValues()
        values.put(User_id,product.email)
        values.put(dYear,product.dYear)
        values.put(dMonth,product.dMonth)
        values.put(dDay,product.dDay)
        values.put(Title,product.title)
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
