package com.example.alkoplan

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val notificationId = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
       val notification: Notification = NotificationCompat.Builder(context, channelID)
           .setSmallIcon(R.drawable.alkoplan_icon)
           .setContentTitle(intent.getStringExtra(titleExtra))
           .setContentText(intent.getStringExtra(messageExtra))
           .build()

       val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }
}