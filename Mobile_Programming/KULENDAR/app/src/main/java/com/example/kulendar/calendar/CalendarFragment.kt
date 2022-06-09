package com.example.kulendar.calendar

import android.content.Context
import android.content.Intent
import java.io.FileOutputStream
import android.view.View
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuleander.calendar.MyCalendarAdapter
import com.example.kuleander.calendar.MyCalendarData
import com.example.kulendar.DB.MYDBHelper_Schedule
import com.example.kulendar.DB.MYDBHelper_User
import com.example.kulendar.DB.Schedule
import com.example.kulendar.R
import com.example.kulendar.TableActivity
import com.example.kulendar.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {
    var userID: String = "userID"
    lateinit var binding: FragmentCalendarBinding
    lateinit var adapter: MyCalendarAdapter
    private val data:ArrayList<MyCalendarData> = ArrayList()
    lateinit var MyDBHelper: MYDBHelper_Schedule
    lateinit var calendarView: CalendarView
    lateinit var saveBtn:Button
    lateinit var contextEditText: EditText
    lateinit var btnTtable:Button
    val tNow=System.currentTimeMillis()
    val tDate = Date(tNow)
    val formatYear = SimpleDateFormat("yyyy")
    val formatMonth = SimpleDateFormat("M")
    val formatDay = SimpleDateFormat("d")
    var sYear=formatYear.format(tDate)
    var sMonth=formatMonth.format(tDate)
    var sDay=formatDay.format(tDate)
    var chYear=sYear.toInt()
    var chMonth=sMonth.toInt()
    var chDay=sDay.toInt()
    lateinit var activity: Context
    lateinit var emailID:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val email = arguments?.getString("EMAIL")
        emailID=email!!
        activity=container!!.context
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        initDB()
        init()
        initRecyclerView()
        getDateRecord(chYear,chMonth,chDay)
        return binding.root
    }

    private fun initDB() {
        val dbfile=activity?.getDatabasePath("sechedule.db")

        if(!dbfile!!.parentFile.exists()){
            dbfile.parentFile.mkdir()
        }
        if(!dbfile.exists()){
            val file=resources.openRawResource(R.raw.schedule)
            val fileSize=file.available()
            val buffer=ByteArray(fileSize)
            file.read(buffer)
            file.close()
            dbfile.createNewFile()
            val output=FileOutputStream(dbfile)
            output.write(buffer)
            output.close()
        }
    }

    private fun getDateRecord(gYear:Int,gMonth:Int,gDay:Int){
        data.clear()
        val str = gYear.toString()+gMonth.toString()+gDay.toString()
        data.addAll(MyDBHelper.getDateRecord(gYear,gMonth,gDay,emailID))
//        initRecyclerView()
        adapter.updateRecycleerView()
    }

    private fun showTimetable() {
    }

    private fun init() {
        MyDBHelper= MYDBHelper_Schedule(activity)
//        MyDBHelper.onUpgrade(MyDBHelper.writableDatabase,0,0)
    // UI값 생성
        calendarView=binding.calendarView    //달력
        saveBtn=binding.saveBtn              //저장버튼
        contextEditText=binding.titleInput  //일정 입력
        btnTtable=binding.btnTtable

        chYear=sYear.toInt()
        chMonth=sMonth.toInt()
        chDay=sDay.toInt()

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            saveBtn.visibility = View.VISIBLE
            contextEditText.visibility = View.VISIBLE
            contextEditText.setText("")
            chYear=year
            chMonth=month+1
            chDay=dayOfMonth
            val str=chYear.toString()+chMonth.toString()+chDay.toString()  //바뀐날짜
            getDateRecord(chYear,chMonth,chDay)
        }

        saveBtn.setOnClickListener {
            //저장하기 코드
            val cdate=chYear.toString()+chMonth.toString()+chDay.toString() //변경된 날짜
            val str = contextEditText.text.toString()
            contextEditText.text=null
            val product = Schedule(emailID, cdate, str)
            val result = MyDBHelper.insertProduct(product)
            if (result) {
                getDateRecord(chYear,chMonth,chDay)
                adapter.updateRecycleerView()
//                Toast.makeText(activity, "Data INSERT SUCCESS", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Data INSERT FAILED", Toast.LENGTH_SHORT).show()
            }
        }
        btnTtable.setOnClickListener {
            showTimetable()
        }
    }

    private fun initRecyclerView(){
        binding.recyclerView.layoutManager=
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        adapter= MyCalendarAdapter(data)
        binding.recyclerView.adapter = adapter

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                adapter.moveItem(p1.adapterPosition,p2.adapterPosition)
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var num=viewHolder.adapterPosition
                //삭제하기 코드
                val result=MyDBHelper.deleteProduct(data[num].pid)
                if(result){
//                    Toast.makeText(activity,"Data DELETE SUCCESS",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity,"Data DELETE FAILED",Toast.LENGTH_SHORT).show()
                }
                adapter.removeItem(viewHolder.adapterPosition)
                data.clear()
                getDateRecord(chYear,chMonth,chDay)
            }
        }

        val itemTouchHelper= ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
}