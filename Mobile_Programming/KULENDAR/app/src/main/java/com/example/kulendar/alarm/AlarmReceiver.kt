package com.example.kulendar.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kulendar.R

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1000" // 알림이 발생하는 채널
    }

    override fun onReceive(context: Context, intent: Intent) {
        val nid = intent.getLongExtra("nid", 0)
        val date = intent.getStringExtra("date")
        val title = intent.getStringExtra("title")
        val alarmModel = AlarmModel(nid, date!!, title!!)
        // 채널 생성 함수
        createNotificationChannel(context)

        // 알림 전송 함수
        notifyNotification(context, alarmModel)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Alarm notification",
                NotificationManager.IMPORTANCE_HIGH
            )

            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    private fun notifyNotification(context: Context, alarmModel: AlarmModel) {
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(alarmModel.date)
                .setContentText(alarmModel.title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setAutoCancel(true)

            notify(alarmModel.nid.toInt(), build.build())
        }

    }

}