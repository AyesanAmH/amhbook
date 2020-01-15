package com.aungmoehein.moehein.event

import android.content.Context
import androidx.appcompat.app.AlertDialog
import me.myatminsoe.mdetect.MDetect
import java.util.*

object EventUtils {

    fun mDay(day:String):String{
        return when(day){
            "2" -> MDetect.getText("တနင်္လာ")
            "3" -> MDetect.getText("အင်္ဂါ")
            "4" -> MDetect.getText("ဗုဒ္ဓဟူး")
            "5" -> MDetect.getText("ကြာသပတေး")
            "6" -> MDetect.getText("သောကြာ")
            "7" -> MDetect.getText("စ နေ")
            "1" -> MDetect.getText("တနင်္ဂနွေ")
            else -> ""
        }
    }

    fun mAMPM(hour:Int):String{
        if(hour >= 3 && hour <= 10)
            return MDetect.getText("နံနက်")

        else if(hour >= 11 && hour <= 14)
            return MDetect.getText("နေ့လယ်")

        else if(hour >= 15 && hour <=18 )
            return MDetect.getText("ည နေ")

        else if(hour <= 2 || (hour >= 19 && hour <= 23))
            return MDetect.getText("ည")

        else return ""

    }

    fun showTime(calendar: Calendar):String{
        val am_pm:String
        val am_pm_hour:String
        val am_pm_minute:String
        val time = calendar.time

        if(time.hours < 12) am_pm = "AM" else am_pm="PM"


        if(time.hours<10) am_pm_hour = "0${time.hours}"

        else if(time.hours > 12) am_pm_hour = (time.hours-12).toString()

        else am_pm_hour = time.hours.toString()


        if (time.minutes < 10)
            am_pm_minute = "0${time.minutes}"
        else
            am_pm_minute = time.minutes.toString()

        return "$am_pm_hour : $am_pm_minute $am_pm"

    }


    fun expired_time(context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(MDetect.getText("အသိပေးချက်"))
        builder.setMessage(MDetect.getText("လူကြီးမင်း၏  စာပေအခမ်းအနား  ကျင်းပချိန်သည် ကျော်လွန်ခဲ့ပြီဖြစ်ပါသဖြင့် ကြိုတင်အသိပေးခြင်း  မပြုနိူင်ပါ။ "))
        builder.setPositiveButton("OK"){dialogInterface, i ->  }
        builder.show()
    }

    fun expired_alarm(context : Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(MDetect.getText("အသိပေးချက်"))
        builder.setMessage(MDetect.getText("လူကြီးမင်း၏ သတ်မှတ်ချိန်သည် ကျော်လွန်ခဲ့ပြီဖြစ်ပါသဖြင့် ကြိုတင်အသိပေးခြင်း  မပြုနိူင်ပါ။ "))
        builder.setPositiveButton("OK"){dialogInterface, i ->  }
        builder.show()
    }

    fun set_no_time(context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(MDetect.getText("အသိပေးချက်"))
        builder.setMessage(MDetect.getText("ကြိုတင်အသိပေးနိူင်ရန်အတွက်  စာပေအခမ်းအနား  ကျင်းပချိန် သတ်မှတ်ပေးပါ။ "))
        builder.setPositiveButton("OK"){dialogInterface, i ->  }
        builder.show()
    }


}