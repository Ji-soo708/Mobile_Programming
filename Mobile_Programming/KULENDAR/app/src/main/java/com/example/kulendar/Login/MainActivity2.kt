package com.example.kulendar.Login

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kulendar.R
import com.example.kulendar.alarm.AlarmFragment
import com.example.kulendar.calendar.CalendarFragment
import com.example.kulendar.dday.DdayFramgent
import com.example.kulendar.home.HomeFragment


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
}