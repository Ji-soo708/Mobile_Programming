package com.example.kulendar.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kulendar.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.parser.Parser


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var myAdapter:HomeRecyclerViewAdapter

    var url="http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=notice&p_rel=1&sort=6&p="; // (&p_rel=1&sort=6&p=페이지&p_rel=1) 학사 국제 학생 일반
    var url2="http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=11688412&p_rel=1&sort=6&p=" //장학
    var url3="http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=11731332&p_rel=17&sort=6&p="//  취업진로
    var scope= CoroutineScope(Dispatchers.IO)
    var searchViewTextListener: SearchView.OnQueryTextListener=
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
                // 검색버튼 입력시 호출
            }

            override fun onQueryTextChange(s: String): Boolean {
                myAdapter.getFilter().filter(s)
                Log.i("change","$s")
                return false
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val email = arguments?.getString("EMAIL")
        Log.d("정보광장 이메일 ","${email} 도착")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }
    fun getNotice(){
        scope.launch {
            for (i in 0..5) {
                val doc = Jsoup.connect(url+i.toString()).parser(Parser.xmlParser()).get()
                val notice = doc.select("tr") // td.gb

                for (t in notice) {
                    var type = t.select("td.gb")
                    Log.i("ffffffff", type.text())
                    var title = t.select("td.subject a")
                    for (ko in title) {
                        Log.i("ffffffff", ko.text())
                        Log.i("ffffffff", ko.absUrl("href"))
                        myAdapter.items.add(MyNotice(type.text(), ko.text(), ko.absUrl("href")))
                    }
                    withContext(Dispatchers.Main) {
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }
            for (i in 0..11) {
                val doc = Jsoup.connect(url2+i.toString()).parser(Parser.xmlParser()).get()
                val notice = doc.select("tr") // td.gb

                for (t in notice) {

                    var title = t.select("td.subject a")
                    for (ko in title) {
                        Log.i("ffffffff", ko.text())
                        Log.i("ffffffff", ko.absUrl("href"))
                        myAdapter.items.add(MyNotice("장학", ko.text(), ko.absUrl("href")))
                    }
                    withContext(Dispatchers.Main) {
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }
            for (i in 0..17) {
                val doc = Jsoup.connect(url3+i.toString()).parser(Parser.xmlParser()).get()
                val notice = doc.select("tr") // td.gb

                for (t in notice) {
                    var type = t.select("td.gb")
                    Log.i("ffffffff", type.text())
                    var title = t.select("td.subject a")
                    for (ko in title) {
                        Log.i("ffffffff", ko.text())
                        Log.i("ffffffff", ko.absUrl("href"))
                        myAdapter.items.add(MyNotice(type.text(), ko.text(), ko.absUrl("href")))
                    }
                    withContext(Dispatchers.Main) {
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    fun init(){
        getNotice()
        binding.searchv.setOnQueryTextListener(searchViewTextListener)
        binding.htab1RecyclerView.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        binding.htab1RecyclerView.addItemDecoration(
            DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL)
        )
        myAdapter= HomeRecyclerViewAdapter(ArrayList<MyNotice>())
        myAdapter.nItemClickListener=object:HomeRecyclerViewAdapter.OnItemClickListener{

            override fun onItemClick(position: Int) {
                val intent= Intent(Intent.ACTION_VIEW, Uri.parse(myAdapter.items[position].notice_Url))
                startActivity(intent)
            }
        }
        binding.htab1RecyclerView.adapter=myAdapter


    }




}
