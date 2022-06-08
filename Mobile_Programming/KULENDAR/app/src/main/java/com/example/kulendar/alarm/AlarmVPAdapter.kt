package com.example.kulendar.alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kulendar.calendar.CalendarFragment
import com.example.kulendar.home.HomeFragment

class AlarmVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

//    val afragment1= AlarmTab1Fragment()
//    val afragment2= AlarmTab2Fragment()
//
//    val email:String=email
//
//    val bundle = Bundle()
//
//    bundle.putString("EMAIL", email)
//
//    afragment1.arguments=bundle
//    afragment2.arguments=bundle
    override fun getItemCount(): Int=2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> AlarmTab1Fragment()
            else -> AlarmTab2Fragment()
        }
    }
}