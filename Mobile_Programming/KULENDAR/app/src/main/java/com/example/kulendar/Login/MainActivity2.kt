package com.example.kulendar.Login

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kulendar.DB.Alarm
import com.example.kulendar.DB.UserDatabase
import com.example.kulendar.R
import com.example.kulendar.alarm.AlarmFragment
import com.example.kulendar.alarm.AlarmReceiver
import com.example.kulendar.calendar.CalendarFragment
import com.example.kulendar.databinding.ActivityMainBinding
import com.example.kulendar.dday.DdayFramgent
import com.example.kulendar.home.HomeFragment
import java.util.*


class MainActivity2 : AppCompatActivity() {

    /////////////BottomNavigation 액티비티//////////////////
    override fun onCreate(savedInstanceState: Bundle?) {

        //intent작업
        //activity->fragment는 intent 사용이 불가해서 bundle 씁니다
        val email = intent.getStringExtra("Email_Main")!!
        if (email!=null){
            Log.d("Email_Main2",email)
        }

        val fragment1=HomeFragment()
        val fragment2=CalendarFragment()
        val fragment3=DdayFramgent()
        val fragment4=AlarmFragment()

        val bundle = Bundle()
        bundle.putString("EMAIL",email)

        fragment1.arguments=bundle
        fragment2.arguments=bundle
        fragment3.arguments=bundle
        fragment4.arguments=bundle

        //아래 bnv_main.run코드도 수정됐습니다.

        //BottomNavigation 구현 코드

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // 하단 탭이 눌렸을 때 화면을 전환하기 위해선 이벤트 처리하기 위해 BottomNavigationView 객체 생성
        var bnv_main = findViewById(R.id.bnv_main) as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.first -> {
                    // 다른 프래그먼트 화면으로 이동하는 기능
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment1).commit()
                }
                R.id.second -> {
                    val calendarFragment = CalendarFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment2).commit()
                }
                R.id.third -> {
                    val ddayFragment = DdayFramgent()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment3).commit()
                }
                R.id.forth -> {
                    val alarmFragment = AlarmFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment4).commit()
                }
            }
            true
        }
            selectedItemId = R.id.first
        }
    }


    fun setAlarm(alarmData: Alarm) {

        var alarm = alarmData
        var nid = alarm.notification_ID
        var date = alarm.date
        var title = alarm.title
        var email = alarm.email

        if(nid == 0){
            // 알림 등록
            val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("nid", nid)
            intent.putExtra("date", date)
            intent.putExtra("title", title)
            intent.putExtra("email", email)

            val pendingIntent = PendingIntent.getBroadcast(
                this,
                nid, // pendingIntent Requestcode값을 현재시간으로 줘서 중복 안되게 설정
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 5000, // * 1000을 해야 초 단위
                pendingIntent
            )

            Log.d("TEST", "Notify Test Alarm after 5 seconds")

        } else {

            var dateArr = alarmData.date.split(".")
            var selectedDate = Calendar.getInstance()
            selectedDate.set(dateArr[0].toInt(), dateArr[1].toInt() - 1, dateArr[2].toInt(), 21,0,0)

            // 알림 등록
            val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("nid", nid)
            intent.putExtra("date", date)
            intent.putExtra("title", title)
            intent.putExtra("email", email)

            val pendingIntent = PendingIntent.getBroadcast(
                this,
                nid, // pendingIntent Requestcode값을 현재시간으로 줘서 중복 안되게 설정
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    selectedDate.timeInMillis - 86400000, // 86400000 = 24* 60 * 60 = 1일
                    pendingIntent
                )
            }

            Log.d("TAG-ALARM", "SET ALARM - UserID:${alarmData.email} date:$date title: $title")
        }
    }


    fun repeatAlarm(alarmData: Alarm) {

        // 선택한 날짜 가져오기
        var dateArr = alarmData.date.split(".")
        var selectedDate = Calendar.getInstance()
        selectedDate.set(dateArr[0].toInt(), dateArr[1].toInt() - 1, dateArr[2].toInt(), 21,0,0)

        // 현재 날짜 가져오기
        var today = Calendar.getInstance()
        var currentHour = today.get(Calendar.HOUR_OF_DAY)

        if(currentHour < 21) {

            var currentDate = today
            currentDate.set(Calendar.HOUR_OF_DAY, 21)
            currentDate.set(Calendar.MINUTE, 0)
            currentDate.set(Calendar.SECOND, 0)

            // 두 날짜 간 차이 계산
            var calcuDate = ((currentDate.timeInMillis - selectedDate.timeInMillis)/86400000).toInt()

            var intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("nid",alarmData.notification_ID)
            intent.putExtra("date",alarmData.date)
            intent.putExtra("title",alarmData.title)

            var alarmManager:AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            for (i:Int in 1..calcuDate step(1)) {
                var pendingIntent = PendingIntent.getBroadcast(
                    this,
                    alarmData.notification_ID + i,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    currentDate.timeInMillis + (i * 86400000), // 알림 발동 시간
                    pendingIntent
                )
            }

        } else {

            var currentDate = today
            currentDate.add(Calendar.DAY_OF_MONTH, 1)
            currentDate.set(Calendar.HOUR_OF_DAY, 21)
            currentDate.set(Calendar.MINUTE, 0)
            currentDate.set(Calendar.SECOND, 0)

            // 두 날짜 간 차이 계산
            var calcuDate = ((currentDate.timeInMillis - selectedDate.timeInMillis)/86400000).toInt()

            var intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("nid",alarmData.notification_ID)
            intent.putExtra("date",alarmData.date)
            intent.putExtra("title",alarmData.title)
            intent.putExtra("email",alarmData.email)

            var alarmManager:AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            for (i:Int in 1..calcuDate step(1)) {
                var pendingIntent = PendingIntent.getBroadcast(
                    this,
                    alarmData.notification_ID + i,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    currentDate.timeInMillis + (i * 86400000), // 알림 발동 시간
                    pendingIntent
                )
            }
        }


    }

    //프래그먼트에 데이터 전달하기
    fun setDataAtFragment(fragment:Fragment, email:String) {
        val bundle = Bundle()
        bundle.putString("EMAIL", email)

        fragment.arguments = bundle
        setFragment(fragment)
    }

    //프래그먼트 띄우기
    fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.commit()
    }
}