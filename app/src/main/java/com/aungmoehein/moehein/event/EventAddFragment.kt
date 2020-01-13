package com.aungmoehein.moehein.event


import android.graphics.Color
import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AlertDialog

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.Event
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.android.synthetic.main.event_list_layout.*
import kotlinx.android.synthetic.main.event_remainder_day.view.*
import kotlinx.android.synthetic.main.event_time_layout.view.*
import kotlinx.android.synthetic.main.fragment_event_add.*
import kotlinx.android.synthetic.main.fragment_event_add.event_name
import kotlinx.android.synthetic.main.fragment_event_add.event_place
import kotlinx.android.synthetic.main.fragment_event_add.event_time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import org.jetbrains.anko.hintTextColor
import org.jetbrains.anko.support.v4.toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class EventAddFragment : Fragment() {
    var check_alarm_time = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        event_name.hint = MDetect.getText("စာပေအခမ်းအနား")
        event_time.hint = MDetect.getText("ကျင်းပချိန်")
        event_place.hint = MDetect.getText("နေရာ")


        lateinit var dialog:View
        val event_day_calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-M-dd")
        var check_event_time  = true
        val event_remainder_calendar = Calendar.getInstance()



        //calendar
        event_calendar.setOnDateChangeListener { calendarView, year, month, day ->
            event_day_calendar.set(Calendar.YEAR, year )
            event_day_calendar.set(Calendar.MONTH, month)
            event_day_calendar.set(Calendar.DAY_OF_MONTH, day)

            event_remainder_calendar.set(Calendar.YEAR,year)
            event_remainder_calendar.set(Calendar.MONTH,month)
            event_remainder_calendar.set(Calendar.DAY_OF_MONTH,day)

            clear_alarm()
        }


        //time
        event_time.setOnClickListener {
            dialog = LayoutInflater.from(context).inflate(R.layout.event_time_layout,null)
            val builder = AlertDialog.Builder(context!!).setView(dialog)
            val alertDialog =  builder.show()

            dialog.event_time_picker.setOnTimeChangedListener { timePicker, hour, minute ->
                event_day_calendar.set(Calendar.HOUR_OF_DAY,hour)
                event_day_calendar.set(Calendar.MINUTE,minute) }


            dialog.event_time_cancel.setOnClickListener { alertDialog.dismiss() }
            dialog.event_time_save.setOnClickListener {
                event_time.text = showTime(event_day_calendar)
                check_event_time = false
                alertDialog.dismiss() }
        }

        //clear remainder
        event_remainder.text = MDetect.getText("ကြိုတင်အသိပေးချိန် သတ်မှတ်မည်")
        event_remainder_clear.setOnClickListener {
            clear_alarm()
        }


        event_remainder.setOnClickListener {
            if(check_event_time)
                set_no_time()

            else if(event_day_calendar.timeInMillis  < Calendar.getInstance().timeInMillis)
                expired_time()

            else{
                dialog = LayoutInflater.from(context).inflate(R.layout.event_remainder_day,null)
                val builder = AlertDialog.Builder(context!!).setView(dialog)
                val alertDialog =  builder.show()

                val days  = mutableListOf<String>()
                days.add(MDetect.getText("ကျင်းပသည့်နေ့"))
                //currentTimeInMills < 24 hours
                val current_calendar = Calendar.getInstance()
                current_calendar.set(Calendar.HOUR_OF_DAY,0)
                current_calendar.set(Calendar.MINUTE,0)
                current_calendar.set(Calendar.SECOND,0)


                //days before event
                var day = 1
                var diffDays = (event_day_calendar.timeInMillis - current_calendar.timeInMillis)/ (24*60*60*1000)
                while (diffDays>0){
                    days.add(MDetect.getText("${myanNum(day.toString())}ရက်အလို"))
                    day++
                    --diffDays }

                dialog.event_remainder_day_picker.minValue = 0
                dialog.event_remainder_day_picker.maxValue = days.size - 1
                dialog.event_remainder_day_picker.displayedValues = days.toTypedArray()


                //save remainder
                dialog.event_remainder_day_picker.setOnValueChangedListener { numberPicker, previous, now ->
                    val sub_day = now*24*60*60*1000
                    val remaind_date = event_day_calendar.timeInMillis - sub_day
                    val formatted_remaind_day = dateFormat.format(remaind_date).split("-")

                    val remaind_year = formatted_remaind_day[0]
                    val remaind_month = formatted_remaind_day[1]
                    val remaind_day = formatted_remaind_day[2]

                    event_remainder_calendar.set(Calendar.YEAR,remaind_year.toInt())
                    event_remainder_calendar.set(Calendar.MONTH,remaind_month.toInt()-1)
                    event_remainder_calendar.set(Calendar.DAY_OF_MONTH,remaind_day.toInt()) }


                //Time Change
                dialog.event_remainder_time_picker.setOnTimeChangedListener { timePicker, hour, minute ->
                    event_remainder_calendar.set(Calendar.HOUR_OF_DAY,hour)
                    event_remainder_calendar.set(Calendar.MINUTE,minute) }


                //Cancel
                dialog.event_remainder_cancel.setOnClickListener {
                    alertDialog.dismiss() }


                //OK
                dialog.event_remainder_save.setOnClickListener {
                    if(event_remainder_calendar.timeInMillis < Calendar.getInstance().timeInMillis)
                        expired_alarm()
                    else{
                        val namedMonth= event_remainder_calendar.getDisplayName(
                            Calendar.MONTH,
                            Calendar.LONG,
                            Locale.getDefault())
                        val show_remaind_day = "${event_remainder_calendar.get(Calendar.DAY_OF_MONTH)}-" +
                                "${event_remainder_calendar.get(Calendar.MONTH)+1}-" +
                                "${event_remainder_calendar.get(Calendar.YEAR)}"
                        event_remainder.text = "$show_remaind_day ($namedMonth - ${showTime(event_remainder_calendar)})"

                        AlarmUtils.setAlarm(context!!,event_remainder_calendar.timeInMillis)
                        event_remainder_clear.visibility = View.VISIBLE
                        check_alarm_time = false
                        alertDialog.dismiss()
                    }

                }
            }

        }

        event_save.setOnClickListener {

            val save_name = event_name.text.toString()
            val save_place = event_place.text.toString()

            val save_date_year = myanNum(event_day_calendar.get(Calendar.YEAR).toString())
            val save_date_month = myanNum(event_day_calendar.get(Calendar.MONTH+1).toString())
            val save_date_day = myanNum(event_day_calendar.get(Calendar.DAY_OF_MONTH).toString())
            val save_date = "$save_date_day.$save_date_month.$save_date_year"


            val namedDay = event_remainder_calendar.get(Calendar.DAY_OF_WEEK)
            val save_time_day = mDay(namedDay.toString())
            val divided_time = event_day_calendar.get(Calendar.HOUR_OF_DAY)
            val save_mm_time = mAMPM(divided_time)
            val hour = event_day_calendar.get(Calendar.HOUR_OF_DAY)
            val formatted_hour = if(hour > 12) hour -12 else hour
            val minute = event_day_calendar.get(Calendar.MINUTE)
            val save_time = "$save_time_day ( $save_mm_time - ${myanNum(formatted_hour.toString())} : ${myanNum(minute.toString())} ) နာရီ"


            val namedMonth= event_remainder_calendar.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.getDefault())
            val save_remaind_day = "${event_remainder_calendar.get(Calendar.DAY_OF_MONTH)}-" +
                    "${event_remainder_calendar.get(Calendar.MONTH)+1}-" +
                    "${event_remainder_calendar.get(Calendar.YEAR)}"
            var save_str_alarm = "$save_remaind_day ($namedMonth - ${showTime(event_remainder_calendar)})"


            if(save_name.isBlank()){
                event_name.hint = MDetect.getText("စာပေအခမ်းအနားအမည် သတ်မှတ်ပေးပါ")
                event_name.hintTextColor = Color.RED }
            else if(check_event_time){
                event_time.hint = MDetect.getText("ကျင်းပချိန် သတ်မှတ်ပေးပါ")
                event_time.hintTextColor = Color.RED }
            else if(save_place.isBlank()){
                event_place.hint = MDetect.getText("နေရာသတ်မှတ်ပေးပါ")
                event_place.hintTextColor = Color.RED }

            else{

                if(check_alarm_time)
                    save_str_alarm = MDetect.getText("ကြိုတင်အသိပေးချိန်သတ်မှတ်ထားခြင်းမရှိပါ")

                //no check conflict (event like DevCon)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val event = Event(name = save_name, time = save_time, date = save_date, place = save_place, strAlarm = save_str_alarm, mlsAlarm = event_remainder_calendar.timeInMillis)
                    db.eventDao().insertEvent(event)
                }
                activity!!.onBackPressed()
            }


        }

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

        if (time.minutes < 10) am_pm_minute = "0${time.minutes}"
        else am_pm_minute = time.minutes.toString()

        return "$am_pm_hour : $am_pm_minute $am_pm"

    }


    fun myanNum(num:String):String{
        val mNum = StringBuilder()
        num.forEach {
            when(it){
                '0' -> mNum.append("၀")
                '1' -> mNum.append("၁")
                '2' -> mNum.append(("၂"))
                '3' -> mNum.append(("၃"))
                '4' -> mNum.append(("၄"))
                '5' -> mNum.append(("၅"))
                '6' -> mNum.append(("၆"))
                '7' -> mNum.append(("၇"))
                '8' -> mNum.append(("၈"))
                '9' -> mNum.append(("၉"))
            }
        }

        return mNum.toString()
    }

    fun mDay(day:String):String{
        return when(day){
            "2" -> MDetect.getText("တနင်္လာ")
            "3" -> MDetect.getText("အင်္ဂါ")
            "4" -> MDetect.getText("ဗုဒ္ဓဟူး")
            "5" -> MDetect.getText("ကြာသပတေး")
            "6" -> MDetect.getText("သောကြာ")
            "7" -> MDetect.getText("စနေ")
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
            return MDetect.getText("ညနေ")

        else if(hour <= 2 || (hour >= 19 && hour <= 23))
            return MDetect.getText("ည")

        else return ""

    }

    fun expired_time(){
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(MDetect.getText("အသိပေးချက်"))
        builder.setMessage(MDetect.getText("လူကြီးမင်း၏  စာပေအခမ်းအနား  ကျင်းပချိန်သည် ကျော်လွန်ခဲ့ပြီဖြစ်ပါသဖြင့် ကြိုတင်အသိပေးခြင်း  မပြုနိူင်ပါ။ "))
        builder.setPositiveButton("OK"){dialogInterface, i ->  }
        builder.show()
    }

    fun expired_alarm(){
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(MDetect.getText("အသိပေးချက်"))
        builder.setMessage(MDetect.getText("လူကြီးမင်း၏ သတ်မှတ်ချိန်သည် ကျော်လွန်ခဲ့ပြီဖြစ်ပါသဖြင့် ကြိုတင်အသိပေးခြင်း  မပြုနိူင်ပါ။ "))
        builder.setPositiveButton("OK"){dialogInterface, i ->  }
        builder.show()
    }

    fun set_no_time(){
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(MDetect.getText("အသိပေးချက်"))
        builder.setMessage(MDetect.getText("ကြိုတင်အသိပေးနိူင်ရန်အတွက်  စာပေအခမ်းအနား  ကျင်းပချိန် သတ်မှတ်ပေးပါ။ "))
        builder.setPositiveButton("OK"){dialogInterface, i ->  }
        builder.show()
    }

    fun clear_alarm(){
        check_alarm_time = true
        event_remainder_clear.visibility = View.GONE
        event_remainder.text = MDetect.getText("ကြိုတင်အသိပေးချိန် သတ်မှတ်မည်")
    }

}



