package com.aungmoehein.moehein.event


import android.graphics.Color
import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AlertDialog

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.Utils
import com.aungmoehein.moehein.Utils.myanNum
import com.aungmoehein.moehein.db.Event
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.event.EventUtils.expired_alarm
import com.aungmoehein.moehein.event.EventUtils.expired_time
import com.aungmoehein.moehein.event.EventUtils.mAMPM
import com.aungmoehein.moehein.event.EventUtils.mDay
import com.aungmoehein.moehein.event.EventUtils.set_no_time
import com.aungmoehein.moehein.event.EventUtils.showTime
import kotlinx.android.synthetic.main.event_list_layout.*
import kotlinx.android.synthetic.main.event_remainder_day.view.*
import kotlinx.android.synthetic.main.event_time_layout.view.*
import kotlinx.android.synthetic.main.fragment_event_add.*
import kotlinx.android.synthetic.main.fragment_event_add.event_name
import kotlinx.android.synthetic.main.fragment_event_add.event_place
import kotlinx.android.synthetic.main.fragment_event_add.event_time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import org.jetbrains.anko.hintTextColor
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.textColor
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

        event_save.text = MDetect.getText("သိမ်းမည်")
        event_cancel.text = MDetect.getText("မသိမ်းတော့ပါ")

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val db = MoeHein.getInstance(context!!)
            val name = MDetect.getStringArray(db.eventDao().getSugName())
            val place  = MDetect.getStringArray(db.eventDao().getSugPlace())

            async(Dispatchers.Main){
                val nameAdapter = ArrayAdapter<String>(context!!,
                    android.R.layout.simple_dropdown_item_1line,name)
                event_name.setAdapter(nameAdapter)
                event_name.threshold = 1
                event_name.onFocusChangeListener = View.OnFocusChangeListener {
                        v, hasFocus -> if(hasFocus) event_name.showDropDown()  }

                val placeAdapter = ArrayAdapter<String>(context!!,
                    android.R.layout.simple_dropdown_item_1line,place)
                event_place.setAdapter(placeAdapter)
                event_place.threshold = 1
                event_place.onFocusChangeListener = View.OnFocusChangeListener {
                        v, hasFocus -> if(hasFocus) event_place.showDropDown() }
            }
        }


        //declare variables
        lateinit var dialog:View
        val dateFormat = SimpleDateFormat("yyyy-M-dd")
        var check_event_time  = true
        val event_day_calendar = Calendar.getInstance()
        val event_remainder_calendar = Calendar.getInstance()



        //calendar date change
        event_calendar.setOnDateChangeListener { calendarView, year, month, day ->
            event_day_calendar.set(Calendar.YEAR, year )
            event_day_calendar.set(Calendar.MONTH, month)
            event_day_calendar.set(Calendar.DAY_OF_MONTH, day)

            event_remainder_calendar.set(Calendar.YEAR,year)
            event_remainder_calendar.set(Calendar.MONTH,month)
            event_remainder_calendar.set(Calendar.DAY_OF_MONTH,day)

            clear_alarm()
        }


        //event day time
        event_time.setOnClickListener {

            //dialog
            dialog = LayoutInflater.from(context).inflate(R.layout.event_time_layout,null)
            val builder = AlertDialog.Builder(context!!).setView(dialog)
            val alertDialog =  builder.show()

            //dialog time picker change
            dialog.event_time_picker.setOnTimeChangedListener { timePicker, hour, minute ->
                event_day_calendar.set(Calendar.HOUR_OF_DAY,hour)
                event_day_calendar.set(Calendar.MINUTE,minute) }


            // cancel time
            dialog.event_time_cancel.setOnClickListener { alertDialog.dismiss() }

            //save event time
            dialog.event_time_save.setOnClickListener {
                event_time.text = showTime(event_day_calendar)
                event_time.textColor = Color.BLACK
                check_event_time = false
                alertDialog.dismiss() }
        }

        //clear remainder
        event_remainder.text = MDetect.getText("ကြိုတင်အသိပေးချိန် သတ်မှတ်မည်")
        event_remainder_clear.setOnClickListener {
            clear_alarm()
        }


        //remainder
        event_remainder.setOnClickListener {

            if(check_event_time)
                set_no_time(context!!)

            else if(event_day_calendar.timeInMillis  < Calendar.getInstance().timeInMillis)
                expired_time(context!!)

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


                //set day picker value
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
                        expired_alarm(context!!)

                    else{
                        val namedMonth= event_remainder_calendar.getDisplayName(
                            Calendar.MONTH, Calendar.LONG, Locale.getDefault())

                        val show_remaind_day = "${event_remainder_calendar.get(Calendar.DAY_OF_MONTH)}-" +
                                "${event_remainder_calendar.get(Calendar.MONTH)+1}-" +
                                "${event_remainder_calendar.get(Calendar.YEAR)}"

                        event_remainder.text = "$show_remaind_day ($namedMonth - ${showTime(event_remainder_calendar)})"
                        event_remainder.textColor = Color.BLACK


                        event_remainder_clear.visibility = View.VISIBLE
                        check_alarm_time = false
                        alertDialog.dismiss()
                    }

                }
            }

        }

        event_cancel.setOnClickListener {
            activity!!.onBackPressed()
        }

        event_save.setOnClickListener {

            //save name and place
            val save_name = event_name.text.toString()
            val save_place = event_place.text.toString()

            //save date
            val save_date_year = myanNum(event_day_calendar.get(Calendar.YEAR).toString())
            val save_date_month = myanNum(event_day_calendar.get(Calendar.MONTH+1).toString())
            val save_date_day = myanNum(event_day_calendar.get(Calendar.DAY_OF_MONTH).toString())
            val save_date = "$save_date_day.$save_date_month.$save_date_year"

            //get mm day
            val namedDay = event_remainder_calendar.get(Calendar.DAY_OF_WEEK)
            val save_time_day = mDay(namedDay.toString())

            //get mm titme
            val divided_time = event_day_calendar.get(Calendar.HOUR_OF_DAY)
            val save_mm_time = MDetect.getText(mAMPM(divided_time))


            //get hour
            val hour = event_day_calendar.get(Calendar.HOUR_OF_DAY)
            var formatted_hour = if(hour > 12) hour - 12  else hour

            //get minute
            val minute = event_day_calendar.get(Calendar.MINUTE)
            val formatted_minute = if( minute < 10) "0"+ minute else minute.toString()


            //save mmtime
            val save_time = "$save_time_day ( $save_mm_time - ${myanNum(formatted_hour.toString())} : ${myanNum(formatted_minute)} ) နာရီ"


            //save month name
            val namedMonth= event_remainder_calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())


            //save remaind day
            val save_remaind_day = "${event_remainder_calendar.get(Calendar.DAY_OF_MONTH)}-" +
                    "${event_remainder_calendar.get(Calendar.MONTH)+1}-" +
                    "${event_remainder_calendar.get(Calendar.YEAR)}"

            var save_str_alarm = "$save_remaind_day ($namedMonth - ${showTime(event_remainder_calendar)})"
            var save_alarm:Long = 0



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
                else
                    save_alarm = event_remainder_calendar.timeInMillis


                //no check conflict (event like Barcamp)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val event = Event(name = Utils.roomText(save_name), day = event_day_calendar.timeInMillis,time = showTime(event_day_calendar), mmtime = save_time, date = save_date,place = Utils.roomText(save_place),strAlarm = save_str_alarm, mlsAlarm = save_alarm,setAlarm = false)
                    db.eventDao().insertEvent(event)
                }
                activity!!.onBackPressed()
            }


        }

    }




    fun clear_alarm(){
        check_alarm_time = true
        event_remainder_clear.visibility = View.GONE
        event_remainder.text = MDetect.getText("ကြိုတင်အသိပေးချိန် သတ်မှတ်မည်")
        event_remainder.textColor = Color.GRAY
    }

}



