package com.example.finalprojectstudyapp

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val calendarNotificationID = 2
const val calendarChannelID = "channel2"
const val eventExtra = "eventExtra"

class CalendarNotification: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, calendarChannelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(eventExtra))
            .setContentText("hey! it's an event you didn't want to miss")
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(calendarNotificationID, notification)
    }
}