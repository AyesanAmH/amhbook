package com.aungmoehein.moehein.event

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log.i
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.Event
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect

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
        val code = p1?.getIntExtra("request_code",1)
        i("request_code",code.toString())

        val mBuilder = NotificationCompat.Builder(p0!!,"AlarmId")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Literature Event")
            .setContentText("Here\'s your alarm \n not forget to go \n ok")
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
            .setDestination(R.id.eventAddFragment)
            .createPendingIntent()
    }
}