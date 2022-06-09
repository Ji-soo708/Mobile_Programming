package com.example.kulendar

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup


import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kulendar.DB.MYDBHelper_Subject
import com.example.kulendar.DB.MyDBHelper_TimeTable
import com.example.kulendar.databinding.ActivityTableBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.simple.parser.ParseException
import org.w3c.dom.Text
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.security.AccessController.getContext
import java.util.*
import javax.security.auth.Subject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TableActivity : AppCompatActivity() {
    lateinit var binding: ActivityTableBinding
    lateinit var myDbHelper: MYDBHelper_Subject
    lateinit var TimeDbHelper:MyDBHelper_TimeTable
    lateinit var SubDbHelper:MYDBHelper_Subject
    lateinit var kindKey:String
    lateinit var univKey:String
    lateinit var sustKey:String
    lateinit var kindValue:String
    lateinit var univValue:String
    lateinit var sustValue:String // request 로 넣을 value 값들
    var flag:Boolean=true
    //data 저장
    var Univdata=HashMap<String,ArrayList<String>>()
    var UnivHash=HashMap<String,String>()
    var SustHash=HashMap<String,String>()
    var KindArray= arrayListOf("전필","전선","지교","지필","일선","기교","심교","융필","융선")
    var UnivArray= arrayListOf("전체대학","공과대학","상경대학","경영대학","글로벌융합대학","상허교양대학","국제처","사회과학대학","KU혁신공유대학")
    var KindHash=HashMap<String,String>()
    var allclass=arrayListOf("전체대학","공과대학","전기공학과","항공우주시스템공학과","융합신소재공학과","화학공학과","생물공학과","유기나노시스템공학과","환경공학과","토목공학과","산업공학과","인프라시스템공학과" , "사회환경플랜트공학과" ,"전자공학과","사회환경공학부","기계공학부","전기전자공학부","화학공학부","소프트웨어학과","컴퓨터공학과","기술융합공학과","기계항공공학부","컴퓨터공학부","경제학전공" ,"국제무역학전공" ,"국제무역학과","경영대학" ,"경영,경영정보학부" ,"경영학전공" ,"경영정보학전공","기술경영학과","경영학과","과학인재전공","상허교양대학","국제처","경제학과","국제무역학과","실감미디어콘텐츠융합전공","실감미디어공학융합전공")
    var engineer= arrayListOf<String>("공과대학","전기공학과","항공우주시스템공학과","융합신소재공학과","화학공학과","생물공학과" ,
        "유기나노시스템공학과", "환경공학과","토목공학과","산업공학과","인프라시스템공학과" , "사회환경플랜트공학과" ,"전자공학과"
        ,"사회환경공학부" ,"기계공학부" ,"전기전자공학부" ,"화학공학부","소프트웨어학과","컴퓨터공학과"
        ,"기술융합공학과","기계항공공학부","컴퓨터공학부" ) //공과대학
    var commerce= arrayListOf<String>("경제학전공" ,"국제무역학전공" ,"국제무역학과" ) //상경대학
    var bussiness= arrayListOf("경영대학" ,"경영,경영정보학부" ,"경영학전공" ,"경영정보학전공","기술경영학과","경영학과") // 경영대학
    var global=arrayListOf("과학인재전공")
    var sanghu=arrayListOf("상허교양대학" )
    var international = arrayListOf("국제처" )
    var socialscience=arrayListOf("경제학과" ,"국제무역학과" )
    var KU = arrayListOf("실감미디어콘텐츠융합전공","실감미디어공학융합전공" )
    ///////////////////
    val scope= CoroutineScope(Dispatchers.IO)
    lateinit var userid:String

    lateinit var myAdapter:subjectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userid = intent.getStringExtra("calendar email")!!
        if(userid != null){
            Log.d("도착 완료: ",userid)
        }
        init()
        initdata()
        initCodedata()
        initSpinner()
        initTimetable()

        binding.button.setOnClickListener {
            myAdapter.items.clear()
            main()
        }

        val email = intent.getStringExtra("calendar email")!!
        if(email != null){
            Log.d("도착 완료: ",email)
        }
    }
    fun deleteDB(user:String, num:String){   //DB 에서 과목삭제

        var result=TimeDbHelper.deleteProduct(user,num)
        if(result==true){
            Toast.makeText(applicationContext,"과목 삭제 성공.",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(applicationContext,"과목 삭제 실패.",Toast.LENGTH_SHORT).show()
        }
    }
    fun deleteTimeTable(deletetime:MutableList<String>){ // 시간표에서 과목 삭제
        var startt=""
        var endt=""
        for(j in 0 until deletetime.size) {
            deletetime[j] = deletetime[j].replace(" ", "")
            if (j % 3 == 0) {
                Log.i("delete",deletetime[j])
                if (deletetime[j][0] == '월') {
                    startt = "mon" + deletetime[j].substring(1, deletetime[j].length)
                } else if (deletetime[j][0] == '화') {
                    startt = "tue" + deletetime[j].substring(1, deletetime[j].length)
                } else if (deletetime[j][0] == '수') {
                    startt = "wen" + deletetime[j].substring(1, deletetime[j].length)
                } else if (deletetime[j][0] == '목') {
                    startt = "thu" + deletetime[j].substring(1, deletetime[j].length)
                } else if (deletetime[j][0] == '금') {
                    startt = "fri" + deletetime[j].substring(1, deletetime[j].length)
                }

            } else if (j % 3 == 1) {
                endt = startt.substring(0, 3) + deletetime[j]
            } else {
                val count = endt.substring(endt.length - 1, startt.length).toInt() - startt.substring(
                    endt.length - 1,
                    endt.length
                ).toInt()
                val date = startt.substring(0, 3)
                val getID = applicationContext.resources.getIdentifier(
                    startt,
                    "id",
                    applicationContext.packageName
                )
                var tv = findViewById<TextView>(getID);
                tv.setText("1")
                tv.setBackgroundColor(Color.parseColor("#ffffff"))
                tv.setOnClickListener { }
                for (i in 1 until count) {
                    var timeid: String
                    var timenum = startt.substring(endt.length - 1, endt.length).toInt() + i
                    if (timenum.toString().length == 1) {
                        timeid = date + "0" + timenum.toString()
                        val getID1 = applicationContext.resources.getIdentifier(
                            timeid,
                            "id",
                            applicationContext.packageName
                        )
                        var tv1 = findViewById<TextView>(getID1);
                        tv1.setText("1")
                        tv1.setBackgroundColor(Color.parseColor("#ffffff"))
                        tv1.setOnClickListener { }
                    } else {
                        timeid = date + timenum.toString()
                        val getID1 = applicationContext.resources.getIdentifier(
                            timeid,
                            "id",
                            applicationContext.packageName
                        )
                        var tv1 = findViewById<TextView>(getID1);
                        tv1.setText("1")
                        tv1.setBackgroundColor(Color.parseColor("#ffffff"))
                        tv1.setOnClickListener { }

                    }
                }
                val getID2 = applicationContext.resources.getIdentifier(
                    endt,
                    "id",
                    applicationContext.packageName
                )
                var tv2 = findViewById<TextView>(getID2);
                tv2.setText("1")
                tv2.setBackgroundColor(Color.parseColor("#ffffff"))
                tv2.setOnClickListener { }
                initTimetable()
            }

        }

    }
    fun timtableColor(name:String,time1:String,time2:String,room:String,rcolor:Int,num:String,deletetime:MutableList<String>){  // 시간표에 과목 추가 ( timetable textview 색상 및 속성변경 )
        val count=time2.substring(time2.length-1,time2.length).toInt()-time1.substring(time2.length-1,time2.length).toInt()
        val date=time1.substring(0,3)
        val getID=applicationContext.resources.getIdentifier(time1,"id",applicationContext.packageName)
        var tv=findViewById<TextView>(getID);
        tv.setText(name+"강의실:"+room)
        tv.setBackgroundColor(rcolor)
        tv.setOnClickListener {
            var builder=AlertDialog.Builder(this)
            builder.setTitle("시간표 삭제")
            builder.setMessage("해당 과목을 삭제하시겠습니까 ?")
            builder.setIcon(R.drawable.logo)
            var listener=object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            deleteDB(userid,num)
                            deleteTimeTable(deletetime)
                        }
                        DialogInterface.BUTTON_NEGATIVE->{
                            Toast.makeText(applicationContext,"해당 시간표를 유지합니다",Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
            builder.setPositiveButton("예", listener)
            builder.setNegativeButton("아니오", listener)
            builder.show()

        }


        for(i in 1 until count){
            var timeid:String
            var timenum=time1.substring(time2.length-1,time2.length).toInt()+i
            if(timenum.toString().length==1){
                timeid=date+"0"+timenum.toString()
                val getID1=applicationContext.resources.getIdentifier(timeid,"id",applicationContext.packageName)
                var tv1=findViewById<TextView>(getID1);
                tv1.setText(name+"강의실:"+room)
                tv1.setBackgroundColor(rcolor)
                tv1.setOnClickListener {var builder=AlertDialog.Builder(this)
                    builder.setTitle("시간표 삭제")
                    builder.setMessage("해당 과목을 삭제하시겠습니까 ?")
                    builder.setIcon(R.drawable.logo)
                    var listener=object:DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            when(which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    deleteDB(userid,num)
                                    deleteTimeTable(deletetime)

                                }
                                DialogInterface.BUTTON_NEGATIVE->{
                                    Toast.makeText(applicationContext,"해당 시간표를 유지합니다",Toast.LENGTH_SHORT).show()

                                }
                            }
                        }
                    }
                    builder.setPositiveButton("예", listener)
                    builder.setNegativeButton("아니오", listener)
                    builder.show()
                }
            }
            else{
                timeid=date+timenum.toString()
                val getID1=applicationContext.resources.getIdentifier(timeid,"id",applicationContext.packageName)
                var tv1=findViewById<TextView>(getID1);
                if(tv1.text.toString()=="1")
                tv1.setText(name+"강의실:"+room)
                tv1.setBackgroundColor(rcolor)
                tv1.setOnClickListener { var builder=AlertDialog.Builder(this)
                    builder.setTitle("시간표 삭제")
                    builder.setMessage("해당 과목을 삭제하시겠습니까 ?")
                    builder.setIcon(R.drawable.logo)
                    var listener=object:DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            when(which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    deleteDB(userid,num)
                                    deleteTimeTable(deletetime)

                                }
                                DialogInterface.BUTTON_NEGATIVE->{
                                    Toast.makeText(applicationContext,"해당 시간표를 유지합니다",Toast.LENGTH_SHORT).show()

                                }
                            }
                        }
                    }
                    builder.setPositiveButton("예", listener)
                    builder.setNegativeButton("아니오", listener)
                    builder.show() }
            }
        }
        val getID2=applicationContext.resources.getIdentifier(time2,"id",applicationContext.packageName)
        var tv2=findViewById<TextView>(getID2);
        tv2.setText(name+"강의실:"+room)
        tv2.setBackgroundColor(rcolor)
        tv2.setOnClickListener { var builder=AlertDialog.Builder(this)
            builder.setTitle("시간표 삭제")
            builder.setMessage("해당 과목을 삭제하시겠습니까 ?")
            builder.setIcon(R.drawable.logo)
            var listener=object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            deleteDB(userid,num)
                            deleteTimeTable(deletetime)
                        }
                        DialogInterface.BUTTON_NEGATIVE->{
                            Toast.makeText(applicationContext,"해당 시간표를 유지합니다",Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
            builder.setPositiveButton("예", listener)
            builder.setNegativeButton("아니오", listener)
            builder.show() }




    }
    fun RandomColor():Int{
        val random = Random()
        return Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256));
    }
    fun checktable(num:String):Boolean {  // 시간표에 과목 추가시 해당 시간에 이미 다른 과목을 수강중인지 중복체크
        var timelist = TimeDbHelper.findTime(userid)
        var timedata1 = SubDbHelper.findTime(num)
        var timedatalist = mutableListOf<String>()
        if (timedata1 == "nodata") {
            Log.i("table", "해당 과목 정보가 없습니다.")

            return false

        } else if (timedata1 == "") {
            Log.i("table", "시간정보가 없습니다.")
            return true
        } else {
            var time = timedata1!!.split(", ", "-", "(").toMutableList()
            Log.i("timedata1", time.toString())
            var startt: String = ""
            var endt: String = ""
            for (j in 0 until time.size) {
                time[j] = time[j].replace(" ", "")
                if (j % 3 == 0) {
                    startt = time[j]
                } else if (j % 3 == 1) {
                    endt = startt.substring(0, 1) + time[j]
                } else { //9~11  < < / > >
                    timedatalist.add(startt)
                    timedatalist.add(endt)

                    Log.i("tabledata", startt) //월02
                    Log.i("tabledata", endt)   //월04
                }
            }
        }
        if (timelist != null) {
            for (i in 0 until timelist.size) {
                var numdata = timelist.get(i)
                var timedata = SubDbHelper.findTime(numdata)
                if (timedata == "nodata") {
                    Log.i("table", "해당 과목 정보가 없습니다.")
                } else if (timedata == "") {
                    Log.i("table", "시간정보가 없습니다.")
                } else {
                    var time = timedata!!.split(", ", "-", "(").toMutableList()
                    Log.i("timedata", time.toString())
                    var startt: String = ""
                    var endt: String = ""
                    for (j in 0 until time.size) {
                        time[j] = time[j].replace(" ", "")
                        if (j % 3 == 0) {
                            startt = time[j]
                        } else if (j % 3 == 1) {
                            endt = startt.substring(0, 1) + time[j]
                        } else {
                            Log.i("tabledata", startt) //월02
                            Log.i("tabledata", endt)   //월04
                            for (i in 0 until timedatalist.size step (2)) {
                                if (timedatalist[i][0] == startt[0]) {
                                    if(timedatalist[i].substring(1,3).toInt()>endt.substring(1,3).toInt()||timedatalist[i+1].substring(1,3).toInt()<startt.substring(1,3).toInt()){
                                        //안겹치는 경우
                                        continue
                                    }
                                    else
                                        return false
                                }
                            }
                        }
                    }
                }

            }
            return true

        }
        else{
            return true

        }
    }

    fun initTimetable(){  // timetable 업데이트
        // 여기에 userid data

        var timelist=TimeDbHelper.findTime(userid)
        if (timelist != null) {
            for(i in 0 until timelist.size){
                var rcolor=RandomColor()
                var numdata=timelist.get(i)
                var timedata=SubDbHelper.findTime(numdata)
                var namedata=SubDbHelper.findName(numdata)

                if(timedata=="nodata"){
                    Log.i("table","해당 과목 정보가 없습니다.")
                }
                else if(timedata==""){
                    Log.i("table","시간정보가 없습니다.")
                }
                else{
                    var time = timedata!!.split(", ","-","(").toMutableList()
                    Log.i("timedata",time.toString())
                    var startt:String=""
                    var endt:String=""
                    var room:String=""
                    for(j in 0 until time.size){
                        time[j]=time[j].replace(" ","")
                        if(j%3==0) {
                            if(time[j][0] =='월') {
                                startt = "mon"+time[j].substring(1,time[j].length)
                            }
                            else if(time[j][0] =='화') {
                                startt = "tue"+time[j].substring(1,time[j].length)
                            }
                            else if(time[j][0] =='수') {
                                startt = "wen"+time[j].substring(1,time[j].length)
                            }
                            else if(time[j][0] =='목') {
                                startt = "thu"+time[j].substring(1,time[j].length)
                            }
                            else if(time[j][0] =='금') {
                                startt = "fri"+time[j].substring(1,time[j].length)
                            }


                        }
                        else if(j%3==1){
                            endt=startt.substring(0,3)+time[j]
                        }
                        else{
                            room=time[j].substring(0,time[j].length-1)
                            Log.i("tabledata",startt)
                            Log.i("tabledata",endt)
                            Log.i("tabledata",room)
                            timtableColor(namedata,startt,endt,room,rcolor,numdata,time)
                        }
                    }
                }
            }
        }
        else
            Toast.makeText(applicationContext,"현재 듣는 과목이 없습니다",Toast.LENGTH_SHORT).show()

    }

    fun init(){
        SubDbHelper=MYDBHelper_Subject(this)
        TimeDbHelper= MyDBHelper_TimeTable(this)

        binding.recyclerView.layoutManager=
            LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
        myAdapter= subjectAdapter(ArrayList())
        myAdapter.itemClickListener=object :subjectAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                // 시간표에 추가 user DB 에 듣는과목에 추가 todo
                if(TimeDbHelper.findProduct(userid,myAdapter.items[position].SubNum)) {
                   Toast.makeText(applicationContext,"이미 추가된 과목입니다.",Toast.LENGTH_SHORT).show()
            //        initTimetable()
            //        TimeDbHelper.deleteProduct(myAdapter.items[position].SubName,myAdapter.items[position].SubNum) deletetest 용
                    TimeDbHelper.getAllRecord()
                }
                else{
                    if(checktable(myAdapter.items[position].SubNum)) {
                        TimeDbHelper.insertProduct(userid, myAdapter.items[position].SubNum)
                        Toast.makeText(applicationContext, "과목 추가 완료", Toast.LENGTH_SHORT).show()
                        initTimetable()
                        TimeDbHelper.getAllRecord()
                    }
                    else{
                        Toast.makeText(applicationContext,"해당시간에 이미 듣는과목이 있습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.recyclerView.adapter=myAdapter

    }
    fun initSpinner(){
        var kindAdapter=ArrayAdapter<String>(this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,KindArray)
        binding.kindspinner.adapter=kindAdapter
        var UnivAdapter=ArrayAdapter<String>(this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,UnivArray)
        binding.Univspinner.adapter=UnivAdapter
        var SustAdapter=ArrayAdapter<String>(this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.Sustspinner.adapter=SustAdapter
        //kind

        binding.kindspinner.setSelection(0)
        binding.kindspinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                kindKey= binding.kindspinner.getItemAtPosition(position).toString()

                Log.i("kind",kindKey)
                if(kindKey=="일선"||kindKey=="기교"||kindKey=="심교"||kindKey=="융필"||kindKey=="융선"){
                    binding.Univspinner.visibility=View.INVISIBLE
                    binding.Sustspinner.visibility=View.INVISIBLE
                    flag=false
                }
                else{
                    binding.Univspinner.visibility=View.VISIBLE
                    binding.Sustspinner.visibility=View.VISIBLE
                    flag=true
                }
                kindValue= KindHash.get(kindKey).toString()

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //univ
        binding.Univspinner.setSelection(0)
        binding.Univspinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                univKey= binding.Univspinner.getItemAtPosition(position).toString()
                univValue= UnivHash.get(univKey).toString()
                Log.i("univ",univKey)
                when(univKey) {
                    "전체대학"-> {
                        SustAdapter.clear()
                        SustAdapter.addAll(allclass)
                        SustAdapter.notifyDataSetChanged()
                        binding.Sustspinner.setSelection(0)
                        sustValue="006751"
                    }
                    "공과대학"->{
                        SustAdapter.clear()
                        SustAdapter.addAll(engineer)
                        SustAdapter.notifyDataSetChanged()
                        binding.Sustspinner.setSelection(0)
                        sustValue="103041"
                    }
                    "상경대학"->{
                        SustAdapter.clear()
                        SustAdapter.addAll(commerce)
                        SustAdapter.notifyDataSetChanged()
                        binding.Sustspinner.setSelection(0)
                        sustValue="103851"
                    }
                    "경영대학"->{
                        SustAdapter.clear()
                        SustAdapter.addAll(bussiness)
                        SustAdapter.notifyDataSetChanged()
                        binding.Sustspinner.setSelection(0)
                        sustValue="105541"
                    }
                    "글로벌융합대학"->{
                        SustAdapter.clear()
                        SustAdapter.addAll(global)
                        SustAdapter.notifyDataSetChanged()
                        binding.Sustspinner.setSelection(0)
                        sustValue="122412"
                    }
                    "상허교양대학"->{
                        SustAdapter.clear()
                        SustAdapter.addAll(sanghu)
                        SustAdapter.notifyDataSetChanged()
                        binding.Sustspinner.setSelection(0)
                        sustValue="126841"

                    }
                    "국제처"->{
                        SustAdapter.clear()
                        SustAdapter.addAll(international)
                        SustAdapter.notifyDataSetChanged()
                        binding.Sustspinner.setSelection(0)
                        sustValue="126940"

                    }
                    "사회과학대학"->{
                        SustAdapter.clear()
                        SustAdapter.addAll(socialscience)
                        SustAdapter.notifyDataSetChanged()
                        binding.Sustspinner.setSelection(0)
                        sustValue="127121"

                    }
                    "KU혁신공유대학"->{
                        SustAdapter.clear()
                        SustAdapter.addAll(KU)
                        SustAdapter.notifyDataSetChanged()
                        binding.Sustspinner.setSelection(0)
                        sustValue="127663"
                    }

                }



            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        // sust

        binding.Sustspinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                sustKey= binding.Sustspinner.getItemAtPosition(position).toString()
                sustValue= SustHash.get(sustKey).toString()
                Log.i("sust",sustKey)
                Log.i("sust",sustValue)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


    }
    fun initCodedata(){
        KindHash.put("전필","B04044");KindHash.put("전선","B04045" );KindHash.put("지교","B04043");KindHash.put("지필","B04061");
        KindHash.put("일선","B04046");KindHash.put("기교","B0404P");KindHash.put("심교","B04054");KindHash.put("융필","B04071");KindHash.put("융선","B04072" );
        //Kind code 완료
        UnivHash.put("전체대학","006751"); UnivHash.put("KU혁신공유대학","127662"); UnivHash.put("경영대학" ,"105541"); UnivHash.put("공과대학","103041");
        UnivHash.put("국제처","126940"); UnivHash.put("글로벌융학대학","122046"); UnivHash.put("사회과학대학","127119"); UnivHash.put("상경대학","103781"); UnivHash.put("상허교양대학","126841");
        // Univ code 완료
        SustHash.put("전체대학","006751"); SustHash.put("공과대학","103041");SustHash.put("전기공학과","103271");SustHash.put("항공우주시스템공학과","120933");SustHash.put("융합신소재공학과","122053");SustHash.put("화학공학과","122054");SustHash.put("생물공학과","122055");
        SustHash.put("유기나노시스템공학과","122056");SustHash.put("환경공학과","122215");SustHash.put("토목공학과","122216");SustHash.put("산업공학과","122227");SustHash.put("인프라시스템공학과","126785");SustHash.put("사회환경플랜트공학과","126901");SustHash.put("전자공학과","126902");
        SustHash.put("사회환경공학부","127108");SustHash.put("기계공학부","127109");SustHash.put("전기전자공학부","127110");SustHash.put("화학공학부","127111");SustHash.put("소프트웨어학과","127113");SustHash.put("컴퓨터공학과","127114");SustHash.put("기술융합공학과","127118");SustHash.put("기계항공공학부","127427");
        SustHash.put("컴퓨터공학부","127428");SustHash.put("경제학전공","103851");SustHash.put("국제무역학전공","103901");SustHash.put("국제무역학과","126782");SustHash.put("경영대학","105541");SustHash.put("경영,경영정보학부","105561");SustHash.put("경영학전공","105591");
        SustHash.put("경영정보학전공","105611");SustHash.put("기술경영학과","121174");SustHash.put("경영학과","126780");SustHash.put("과학인재전공","122412");SustHash.put("상허교양대학","126841");SustHash.put("국제처","126940");SustHash.put("경제학과","127121");SustHash.put("국제무역학과","127123");
        SustHash.put("실감미디어콘텐츠융합전공","127663");SustHash.put("실감미디어공학융합전공","127664");
        // Sust code 완료

    }
    fun initdata(){
        Univdata.put("전체대학",allclass)
        Univdata.put("공과대학",engineer)
        Univdata.put("상경대학",commerce)
        Univdata.put("경영대학",bussiness)
        Univdata.put("글로벌융합대학",global)
        Univdata.put("상허교양대학",sanghu)
        Univdata.put("국제처",international)
        Univdata.put("사회과학대학",socialscience)
        Univdata.put("KU혁신공유대학",KU)
    }
    fun main() {
        scope.launch{
            myDbHelper= MYDBHelper_Subject(this@TableActivity)
            println("==============application start==============")
            Log.i("asdfa",Univdata.toString())

            val timeStamp: Long = Date().getTime()
            val url =
                URL("https://sugang.konkuk.ac.kr/sugang/search?attribute=lectListOuter&fake=$timeStamp")
            val httpConn: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConn.setRequestMethod("POST")
            httpConn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01")
            httpConn.setRequestProperty("Accept-Language", "en-US,en;q=0.9,ko;q=0.8,ko-KR;q=0.7")
            httpConn.setRequestProperty("Connection", "keep-alive")
            httpConn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8")
            httpConn.setRequestProperty("Cookie",
                "_gcl_au=1.1.1845141770.1649757680; WMONID=DxY5tKeu1op; JSUGANGSESSIONID=0001hyk0fx7-iy6Y4MhAllU84QF:-141BJKO")
            httpConn.setRequestProperty("Origin", "https://sugang.konkuk.ac.kr")
            httpConn.setRequestProperty("Referer",
                "https://sugang.konkuk.ac.kr/sugang/jsp/search/searchMainOuter.jsp")
            httpConn.setRequestProperty("Sec-Fetch-Dest", "empty")
            httpConn.setRequestProperty("Sec-Fetch-Mode", "cors")
            httpConn.setRequestProperty("Sec-Fetch-Site", "same-origin")
            httpConn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.61 Safari/537.36")
            httpConn.setRequestProperty("X-Requested-With", "XMLHttpRequest")
            httpConn.setRequestProperty("sec-ch-ua",
                "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"102\", \"Google Chrome\";v=\"102\"")
            httpConn.setRequestProperty("sec-ch-ua-mobile", "?0")
            httpConn.setRequestProperty("sec-ch-ua-platform", "\"macOS\"")
            httpConn.setDoOutput(true)
            val writer = OutputStreamWriter(httpConn.getOutputStream())
            Log.i("valuecheck",kindValue)
            Log.i("valuecheck",univValue)
            Log.i("valuecheck",sustValue)
            var writecode:String
            if(flag==true) { // 전선 전필 ,, 등 등일때
                writecode= "pYear=2022&pTerm=B01014&pPobt=" + kindValue + "&pSearchGb=1&pUniv=" + univValue + "&pSustMjCd=" + sustValue + "&pLang=&pKind=&pSearchKind=1&pSearchNm="
            }
            else{  // 일선 심교 등등..
                writecode= "pYear=2022&pTerm=B01014&pPobt=" + kindValue + "&pSearchGb=1&pLang=&pKind=&pSearchKind=1&pSearchNm=&pSearchKind=1&pSearchNm="
            }

            //         var writecode1="pYear=2022&pTerm=B01014&pPobt=B04045&pSearchGb=1&pUniv=103041&pSustMjCd=103041&pLang=&pKind=&pSearchKind=1&pSearchNm="
            writer.write(writecode)
            writer.flush()
            writer.close()
            httpConn.getOutputStream().close()
            val responseStream: InputStream =
                if (httpConn.getResponseCode() / 100 === 2) httpConn.getInputStream() else httpConn.getErrorStream()
            val s: Scanner = Scanner(responseStream).useDelimiter("\\A")
            val response = if (s.hasNext()) s.next() else ""
            val classinfo=JSONObject(response)
            val rows=classinfo.optJSONArray("rows")
            var i=0
            var tempStr=""
            while(i<rows.length()){
                val jsonObject=rows.getJSONObject(i)
                val year=jsonObject.getString("open_yy")
                val department=jsonObject.getString("open_sust_nm")
                val pobt_num=jsonObject.getString("pobt_div") // (구분 전필 전선 지교..)
                val grade=jsonObject.getString("open_shyr") // 개설학년
                val pbot=jsonObject.getString("pobt_div_nm") // 전선 지교
                val subject_type_num=jsonObject.getString("lt_knd")  // 강의종류 번호 ( 이러닝 ~ )
                val subject_num=jsonObject.getString("sbjt_id")  // 과목번호
                val subject_name=jsonObject.getString("typl_nm")  // 과목 이름
                val score=jsonObject.getString("pnt")  // 학점
                val professor=jsonObject.getString("kor_nm")  // 교수님이름
                val tm=jsonObject.getString("tm")  // 시간 ( 3 )
                val tm_rm=jsonObject.getString("room_nm")  // 시간 , 강의실정보
                val haksu_num=jsonObject.getString("haksu_id") // 학수번호
                val subject_type=jsonObject.getString("lt_knd_nm")  // 강의종류 ( 이러닝 ~ )
                tempStr=year+'/'+department+'/'+pobt_num+'/'+grade+'/'+pbot+'/'+subject_type_num+'/'+subject_num+'/'+subject_name+'/'+score+professor+'/'+tm+'/'+tm_rm+'/'+haksu_num+'/'+subject_type
                Log.i("parse",tempStr)
                myAdapter.items.add(com.example.kulendar.DB.Subject(subject_name,subject_num,tm_rm,professor,score))
                var findresult=myDbHelper.findProduct(subject_num)
                if(findresult) {
                    Log.i("db","DB에 이미 있는 과목")
                }
                else{
                    var dbinsert=com.example.kulendar.DB.Subject(subject_name,subject_num,tm_rm,professor,score)
                    var result=myDbHelper.insertProduct(dbinsert)
                    if(result){
                        Log.i("db","DB 추가 성공")
                    }
                    else{
                        Log.i("db","DB 추가 실패")
                    }
                }
                myDbHelper.getAllRecord()

                i++
            }
            withContext(Dispatchers.Main) {
                myAdapter.notifyDataSetChanged()
            }
            Log.i("data",response)
            println("==============application end==============")

        }
    }


}

}

