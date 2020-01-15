package com.aungmoehein.moehein.event

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmUtils {
    fun setAlarm(context: Context,timeInMills:Long) {

        val broadcastIntent = Intent(context,
            NotificationReceiver::class.java)

        val pIntent = PendingIntent.getBroadcast(
            context, 0, broadcastIntent, 0)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        alarmMgr.set(
            AlarmManager.RTC_WAKEUP,
            timeInMills,
            pIntent
        )
    }


}