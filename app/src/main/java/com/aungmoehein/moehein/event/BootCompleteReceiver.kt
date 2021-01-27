package com.aungmoehein.moehein.event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import com.aungmoehein.moehein.db.Event
import com.aungmoehein.moehein.db.MoeHein
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if(p1?.action == "android.intent.action.BOOT_COMPLETED"){
            BootComplete(p0!!).execute()
        }
    }

    inner class BootComplete(val context: Context) : AsyncTask<Void,Void,List<Event>>(){
        override fun doInBackground(vararg params: Void?): List<Event> {
            val db = MoeHein.getInstance(context)
            return db.eventDao().getReceiverEvent(System.currentTimeMillis())
        }

        override fun onPostExecute(result: List<Event>?) {
            result!!.forEach {
                AlarmUtils.setAlarm(context,it.mlsAlarm,it.id.toInt())
            }
        }
    }

}