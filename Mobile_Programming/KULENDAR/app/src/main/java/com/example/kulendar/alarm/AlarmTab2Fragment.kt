package com.example.kulendar.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Alarm
import com.example.kulendar.DB.AlarmDatabase
import com.example.kulendar.R
import java.io.File
import java.util.*
import javax.mail.Quota

class AlarmTab2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_tab2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var alarmRecyclerView = view.findViewById<RecyclerView>(R.id.alarm_RecyclerView)

        var alarmDB = AlarmDatabase.getInstance(requireContext())
        var alarmList = mutableListOf<Alarm>()

        var savedAlarm = alarmDB!!.alarmDao().getAll()
        if(savedAlarm.isNotEmpty()) {
            alarmList.addAll(savedAlarm)
        }

        var adapter = AlarmRecyclerViewAdapter(alarmList)

        alarmRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.setItemClickListener(object:AlarmRecyclerViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                var alarmData = alarmList[position]

                if(alarmData.repeatOnOff == 0) {
                    alarmData.repeatOnOff += 1

                    view.findViewById<ImageView>(R.id.repeatImage).setImageResource(R.drawable.star_on)

                    var dateArr = alarmData.date.split(".")
                    var selectedDate = Calendar.getInstance()
                    selectedDate.set(dateArr[0].toInt(), dateArr[1].toInt() - 1, dateArr[2].toInt(), 21,0,0)

                    var currentDate = Calendar.getInstance()

                    var calcuDate = ((currentDate.timeInMillis - selectedDate.timeInMillis)/86400000).toInt() + 1

                    var intent = Intent(requireContext(), AlarmReceiver::class.java)
                    intent.putExtra("nid",alarmData.notification_ID)
                    intent.putExtra("date",alarmData.date)
                    intent.putExtra("title",alarmData.title)

                    var alarmManager:AlarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    for (i:Int in 1..calcuDate step(1)) {
                        var pendingIntent = PendingIntent.getBroadcast(
                            requireContext(),
                            alarmData.notification_ID + i,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            currentDate.timeInMillis, // 알림 발동 시간
                            pendingIntent
                        )
                    }

                    alarmDB.alarmDao().update(alarmData)
                    adapter.notifyDataSetChanged()

                    Toast.makeText(requireActivity(),"${alarmData.title} 알림이 중요알리미로 설정되었습니다.",Toast.LENGTH_SHORT).show()
                    Log.d("TAG-ALARM", "START REPEAT ALARM - date:${alarmData.date} title: ${alarmData.title}")

                } else if (alarmData.repeatOnOff == 1) {
                    alarmData.repeatOnOff = alarmData.repeatOnOff - 1

                    view.findViewById<ImageView>(R.id.repeatImage).setImageResource(R.drawable.star_off)

                    var dateArr = alarmData.date.split(".")
                    var selectedDate = Calendar.getInstance()
                    selectedDate.set(dateArr[0].toInt(), dateArr[1].toInt() - 1, dateArr[2].toInt(), 21,0,0)

                    var currentDate = Calendar.getInstance()

                    var calcuDate = ((currentDate.timeInMillis - selectedDate.timeInMillis)/86400000).toInt() + 1

                    for (i:Int in 1..calcuDate step(1)) {
                        var pendingIntent = PendingIntent.getBroadcast(
                            requireContext(),
                            alarmData.notification_ID + i, // 위에서 쓰인 RequestCode 필요
                            Intent(requireContext(),AlarmReceiver::class.java),
                            PendingIntent.FLAG_NO_CREATE
                        )
                        pendingIntent.cancel()
                    }

                    alarmDB.alarmDao().update(alarmData)
                    adapter.notifyDataSetChanged()

                    Toast.makeText(requireActivity(),"${alarmData.title} 알림의 중요알리미 기능이 해제 되었습니다.",Toast.LENGTH_SHORT).show()
                    Log.d("TAG-ALARM", "STOP REPEAT ALARM - date:${alarmData.date} title: ${alarmData.title}")

                }
            }
        })

        // 알림 이동, 삭제
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT){
            override fun onMove (p0: RecyclerView,
                                 p1: RecyclerView.ViewHolder,
                                 p2: RecyclerView.ViewHolder): Boolean {
                adapter.moveItem(p1.adapterPosition, p2.adapterPosition)
                return true
            }
            override fun onSwiped (viewHolder : RecyclerView.ViewHolder,
                                   direction : Int){
                val alarm = alarmList[viewHolder.adapterPosition]
                adapter.removeItem(viewHolder.adapterPosition)
                val pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    alarm.notification_ID, // 이 값의 pendingIntent를 취소
                    Intent(requireContext(), AlarmReceiver::class.java),
                    PendingIntent.FLAG_NO_CREATE
                )
                pendingIntent?.cancel()

                alarmDB?.alarmDao()?.deleteAlarm(alarm = alarm) // DB에서 삭제
                adapter.notifyDataSetChanged() // list 갱신
                Toast.makeText(requireActivity(),"${alarm.title} 알림이 삭제되었습니다.",Toast.LENGTH_SHORT).show()
                Log.d("TAG-ALARM", "REMOVE ALARM($viewHolder.adapterPosition), title:${alarm.title}")
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(alarmRecyclerView)

        alarmRecyclerView.adapter = adapter
    }

}