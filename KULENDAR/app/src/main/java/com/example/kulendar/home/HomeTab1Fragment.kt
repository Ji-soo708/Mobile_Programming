package com.example.kulendar.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kulendar.R
import com.example.kulendar.databinding.FragmentHomeTab1Binding

class HomeTab1Fragment : Fragment() {
    lateinit var binding: FragmentHomeTab1Binding
    private var infoDatas = ArrayList<InfoData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeTab1Binding.inflate(inflater, container, false)

        //임의로 데이터 집어넣음
        infoDatas.add(InfoData("학사", "제목1"))

        val homeRVAdapter = HomeRecyclerViewAdapter(infoDatas)
        binding.htab1RecyclerView.adapter = homeRVAdapter
        binding.htab1RecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        return binding.root
    }
}





