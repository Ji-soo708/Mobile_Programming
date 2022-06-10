package com.example.kulendar.alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kulendar.Login.MainActivity2
import com.example.kulendar.R

class AlarmVPAdapter(fragment: Fragment, email:String): FragmentStateAdapter(fragment) {

    val afragment1= AlarmTab1Fragment()
    val afragment2= AlarmTab2Fragment()

    val email:String=email

    val bundle = Bundle()

    override fun getItemCount(): Int=2

    override fun createFragment(position: Int): Fragment {
        bundle.putString("EMAIL", email)

        afragment1.arguments=bundle
        afragment2.arguments=bundle


        return when(position) {
            0 -> afragment1

            else -> afragment2

        }

    }



}