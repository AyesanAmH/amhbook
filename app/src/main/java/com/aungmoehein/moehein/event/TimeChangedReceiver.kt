package com.aungmoehein.moehein.event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TimeChangedReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if( p1?.action == "android.intent.action.TIME_SET"){
//            Utils.setAlarm(p0!!)
        }
    }
}