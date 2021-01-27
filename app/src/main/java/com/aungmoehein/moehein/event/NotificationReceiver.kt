package com.aungmoehein.moehein.event

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.util.Log.i
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDeepLinkBuilder
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.Event
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.coroutines.*
import me.myatminsoe.mdetect.MDetect

class NotificationReceiver:BroadcastReceiver() {


    override fun onReceive(p0: Context?, p1: Intent?) {

        Noti(p0!!,p1!!).execute()

    }

    fun pendingIntent(context: Context):PendingIntent{
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_bottom)
            .setDestination(R.id.eventAddFragment)
            .createPendingIntent()
    }

    inner class Noti (val context: Context,val intent: Intent) : AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Alarm"
                val descriptionText = "Alarm Details"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel("AlarmId", name, importance)
                mChannel.description = descriptionText


                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
            }
            val code = intent.getIntExtra("request_code", 1)
            val db = MoeHein.getInstance(context!!)
            val event = db.eventDao().getCurrentEvent(code)
            val name = MDetect.getText(event.name)
            val day = MDetect.getText("${event.date} - ${event.mmtime}")



            val mBuilder = NotificationCompat.Builder(context!!, "AlarmId")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(name)
                .setContentText(day)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent(context))




            val am = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val id = System.currentTimeMillis() / 1000
            with(NotificationManagerCompat.from(context)) {
                notify(id.toInt(),mBuilder.build())
            }
            am.notify(id.toInt(), mBuilder.build())
            return true
        }
    }
}