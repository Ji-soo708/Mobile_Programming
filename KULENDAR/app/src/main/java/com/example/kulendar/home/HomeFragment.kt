package com.example.kulendar.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kulendar.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private val TAG="HOMEFragment"

    lateinit var binding: FragmentHomeBinding

    //탭타이틀 설정
    private val tabTitleArray = arrayListOf(
        "정보광장",
        "키워드 등록"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //탭레이아웃과 뷰페이저 연결
        val homeAdapter = HomeVPAdapter(this)
        binding.homeViewPager.adapter = homeAdapter
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        return binding.root //setContentView

    }




}
