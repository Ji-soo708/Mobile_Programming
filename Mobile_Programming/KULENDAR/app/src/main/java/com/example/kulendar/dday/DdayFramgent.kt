package com.example.kulendar.dday

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Dday
import com.example.kulendar.DB.MYDBHelper_User
import com.example.kulendar.DB.MyDBHelper_Dday
import com.example.kulendar.R
import com.example.kulendar.databinding.FragmentDdayFramgentBinding
import java.io.FileOutputStream
import java.util.*

class DdayFramgent : Fragment() {
    /** Called when the activity is first created.  */
    private var ddayText: TextView? = null
    lateinit var binding:FragmentDdayFramgentBinding
    lateinit var adapter:MyDdayAdapter
    private val data:ArrayList<MyDdayData> = ArrayList()
    lateinit var MyDBHelper:MyDBHelper_Dday
    private var saveBtn: Button? = null
    //오늘 연월일 변수
    private var tYear = 0
    private var tMonth = 0
    private var tDay = 0
    //디데이 연월일 변수
    private var dYear = 1
    private var dMonth = 1
    private var dDay = 1
    private var d: Long = 0
    private var t: Long = 0
    private var r: Long = 0
    private var resultNumber = 0
    lateinit var activity: Context
    lateinit var emailID:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val email = arguments?.getString("EMAIL")
        emailID=email!!
        activity=container!!.context
        binding = FragmentDdayFramgentBinding.inflate(inflater, container, false)
        initDB()
        initLayout()
        initRecyclerView()
        getAllRecord()
        return binding.root
    }

    private fun initDB() {
        val dbfile=activity.getDatabasePath("myddaydb.db")

        if(!dbfile.parentFile.exists()){
            dbfile.parentFile.mkdir()
        }
        if(!dbfile.exists()){
            val file=resources.openRawResource(R.raw.myddaydb)
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

    private fun getAllRecord(){
        data.clear()
        data.addAll(MyDBHelper.getAllRecord(emailID))
    }

    private fun initLayout() {
        MyDBHelper= MyDBHelper_Dday(activity)
        saveBtn = binding.saveButton
        saveBtn!!.setOnClickListener {
             var picker = DatePickerDialog(activity, dDateSetListener, tYear, tMonth, tDay)
            picker.show()
        }

        val calendar = Calendar.getInstance() //현재 날짜 불러옴
        tYear = calendar[Calendar.YEAR]
        tMonth = calendar[Calendar.MONTH]
        tDay = calendar[Calendar.DAY_OF_MONTH]
        val dCalendar = Calendar.getInstance()
        dCalendar[dYear, dMonth] = dDay
        t = calendar.timeInMillis //오늘 날짜를 밀리타임으로 바꿈
        d = dCalendar.timeInMillis //디데이날짜를 밀리타임으로 바꿈
        r = (d - t) / (24 * 60 * 60 * 1000) //디데이 날짜에서 오늘 날짜를 뺀 값을 '일'단위로 바꿈
        resultNumber = r.toInt() + 1
    }

    private val dDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dYear = year
            dMonth = monthOfYear
            dDay = dayOfMonth
            val dCalendar = Calendar.getInstance()
            dCalendar[dYear, dMonth] = dDay
            d = dCalendar.timeInMillis
            r = (d - t) / (24 * 60 * 60 * 1000)
            resultNumber = r.toInt()
            val ddayinput=binding.ddayInput.text.toString()
            binding.ddayInput.text=null
            adapter.updateRecycleerView()

            //데이터베이스에 데이터추가하기
            val product=Dday(emailID,dYear,dMonth,dDay,ddayinput)
            val result=MyDBHelper.insertProduct(product)
            if(result){
                getAllRecord()
//                Toast.makeText(activity,"Data INSERT SUCCESS",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity,"Data INSERT FAILED",Toast.LENGTH_SHORT).show()
            }
        }

    private fun initRecyclerView(){
        binding.recyclerView.layoutManager=
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        adapter= MyDdayAdapter(data)

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
                //데이터베이스에서 삭제
                val result=MyDBHelper.deleteProduct(data[num].pid)
                if(result){
//                    Toast.makeText(activity,"Data DELETE SUCCESS",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity,"Data DELETE FAILED"+data[viewHolder.adapterPosition].pid+"hihi",Toast.LENGTH_SHORT).show()
                }
                adapter.removeItem(viewHolder.adapterPosition)
                data.clear()
                getAllRecord()
            }
        }

        val itemTouchHelper= ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
}