package com.example.kulendar.alarm

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlarmVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int=2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> AlarmTab1Fragment()
            else -> AlarmTab2Fragment()
        }
    }
}