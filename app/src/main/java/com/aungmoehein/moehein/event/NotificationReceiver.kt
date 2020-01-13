package com.aungmoehein.moehein.event

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import com.aungmoehein.moehein.R

class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Alarm"
            val descriptionText = "Alarm Details"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("AlarmId",name,importance)
            mChannel.description = descriptionText


            val notificationManager = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
        
        val mBuilder = NotificationCompat.Builder(p0!!,"AlarmId")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarm")
            .setContentText("Here\'s your alarm")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent(p0))

        val am = p0.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = System.currentTimeMillis()/1000
        am.notify(id.toInt(),mBuilder.build())
    }

    fun pendingIntent(context: Context):PendingIntent{
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_bottom)
            .setDestination(R.id.readFragment)
            .createPendingIntent()
    }
}