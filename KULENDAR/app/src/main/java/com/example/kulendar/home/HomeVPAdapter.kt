package com.example.kulendar.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class HomeVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int=2

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> HomeTab1Fragment()
                else -> HomeTab2Fragment()
            }
        }
}