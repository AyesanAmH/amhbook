package com.aungmoehein.moehein.event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if(p1?.action == "android.intent.action.BOOT_COMPLETED"){
//            Utils.setAlarm(p0!!)
        }
    }
}