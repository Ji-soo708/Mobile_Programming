package com.example.kulendar.dday

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kulendar.R


class DdayFramgent : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val email = arguments?.getString("EMAIL")
        Log.d("디데이 이메일 ","${email} 도착")
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dday_framgent, container, false)
    }
}