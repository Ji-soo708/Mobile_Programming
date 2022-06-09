package com.example.kulendar.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Alarm
import com.example.kulendar.DB.AlarmDatabase
import com.example.kulendar.R
import java.util.*


class AlarmTab1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_tab1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var alarmDB = AlarmDatabase.getInstance(requireContext())
        var alarmList = mutableListOf<Alarm>()

        var adapter = AlarmRecyclerViewAdapter(alarmList)

        var saveAlarmBtn = view.findViewById<Button>(R.id.saveAlarmBtn)

        saveAlarmBtn.setOnClickListener {

            var datePicker = view.findViewById<DatePicker>(R.id.datePicker1)
            var year = datePicker.year
            var month = datePicker.month
            var day = datePicker.dayOfMonth

            // 선택한 날짜 생성 (시간은 9 PM)
            var selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day, 21, 0, 0)

            // 현재 날짜 및 시간
            var currentDate = Calendar.getInstance()

            var date:String = year.toString() + "." + (month+1).toString() + "." + day.toString()
            var title:String = view.findViewById<EditText>(R.id.alarm_title).text.toString()
            var alarm = Alarm(currentDate.timeInMillis.toInt(), date, title, 0)

            // 알림 등록
            val alarmManager: AlarmManager =
                requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.putExtra("nid",alarm.notification_ID)
            intent.putExtra("date",alarm.date)
            intent.putExtra("title",alarm.title)

            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                currentDate.timeInMillis.toInt(), // pendingIntent Requestcode값을 현재시간으로 줘서 중복 안되게 설정
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                (selectedDate.timeInMillis / 86400000 - 1),
                pendingIntent
            )

            // 알림 저장
            alarmDB!!.alarmDao().insertAlarm(alarm) // DB에 추가
            alarmList.add(alarm) // list에 추가
            adapter.notifyDataSetChanged()

            Toast.makeText(requireActivity(), "$title alarm set at $date", Toast.LENGTH_SHORT).show()
            Log.d("TAG-ALARM", "SET ALARM - date:${alarm.date} title: ${alarm.title}")
        }

    }

}