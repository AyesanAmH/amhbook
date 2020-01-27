package com.aungmoehein.moehein.event

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmUtils {
    fun setAlarm(context: Context,timeInMills:Long,requestCode : Int) {

        val broadcastIntent = Intent(context,
            NotificationReceiver::class.java)
        broadcastIntent.putExtra("request_code",requestCode)

        val pIntent = PendingIntent.getBroadcast(
            context, requestCode, broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        if(timeInMills > System.currentTimeMillis())
        alarmMgr.set(
            AlarmManager.RTC_WAKEUP,
            timeInMills,
            pIntent
        )
    }

    fun cancelAlarm(context: Context,requestCode: Int){
        val broadcastIntent = Intent(context,
            NotificationReceiver::class.java)
        broadcastIntent.putExtra("request_code",requestCode)

        val pIntent = PendingIntent.getBroadcast(
            context, requestCode, broadcastIntent, 0)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmMgr.cancel(pIntent)
    }





}