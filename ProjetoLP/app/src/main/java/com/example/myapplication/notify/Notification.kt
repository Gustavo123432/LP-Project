package com.example.myapplication.notify

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.myapplication.R

const val NOTIFICATION_ID = 1
const val NOTIFICATION_CHANNEL_ID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notification = NotificationCompat.Builder(context!!, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.meteo)
            .setContentTitle(intent?.getStringExtra(titleExtra))
            .setContentTitle(intent?.getStringExtra(messageExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}