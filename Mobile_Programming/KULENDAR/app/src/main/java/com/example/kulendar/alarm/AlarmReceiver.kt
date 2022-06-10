package com.example.kulendar.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kulendar.R

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1000" // 알림이 발생하는 채널
    }

    override fun onReceive(context: Context, intent: Intent) {
        val nid = intent.getIntExtra("nid", 0)
        val date = intent.getStringExtra("date").toString()
        val title = intent.getStringExtra("title").toString()
        val email = intent.getStringExtra("email").toString()
        val alarmModel = AlarmModel(nid, date, title, email)

        Log.d("ALARMRECEIVER", "Create $title Notification at $date")

        // 채널 생성 함수
        createNotificationChannel(context)

        // 알림 전송 함수
        notifyNotification(context, alarmModel)
    }

    private fun createNotificationChannel(context: Context) {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Alarm notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.setSound(uri, audioAttributes)

            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
        Log.d("ALARM_RECEIVER","CREATE NOTIFICATION CHANNEL")
    }

    private fun notifyNotification(context: Context, alarmModel: AlarmModel) {
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(alarmModel.title)
                .setContentText(alarmModel.date + " set "+alarmModel.email)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setAutoCancel(true)

            notify(alarmModel.nid, build.build())
            Log.d("ALARM_RECEIVER", "BUILD NOTIFICATION")
        }

    }

}