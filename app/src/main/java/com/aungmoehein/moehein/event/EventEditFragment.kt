package com.aungmoehein.moehein.event


import android.graphics.Color
import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.Utils
import com.aungmoehein.moehein.Utils.roomText
import com.aungmoehein.moehein.db.Event
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.android.synthetic.main.event_remainder_day.view.*
import kotlinx.android.synthetic.main.event_time_layout.view.*
import kotlinx.android.synthetic.main.fragment_event_add.*
import kotlinx.android.synthetic.main.fragment_event_edit.*
import kotlinx.android.synthetic.main.fragment_event_edit.event_remainder_clear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import org.jetbrains.anko.hintTextColor
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class EventEditFragment : Fragment() {
    var check_alarm_time = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        event_edit_name.hint = MDetect.getText("စာပေအခမ်းအနား")
        event_edit_place.hint = MDetect.getText("နေရာ")
        event_edit_save.text = MDetect.getText("ပြင်မည်")
        event_edit_cancel.text = MDetect.getText("မပြင်တော့ပါ")


        val args = EventEditFragmentArgs.fromBundle(arguments!!)
        var id = args.id
        val name = args.name
        val time = args.time
        var mmtime = args.mmtime
        val event_day = args.day
        var date = args.date
        val place = args.place
        val stralarm = args.stralarm
        var mlsalarm = args.mlsalarm

        //save values
        var save_edit_name = roomText(name)
        var save_edit_time = roomText(time)
        var save_edit_mm_time = roomText(mmtime)
        var save_edit_event_day = event_day
        var save_edit_date = roomText(date)
        var save_edit_place = roomText(place)
        var save_edit_stralarm = roomText(stralarm)
        var save_edit_mlsalarm = mlsalarm

        i("alarm","$mlsalarm")


//        //set date
        event_edit_calendar.date = event_day
        event_edit_name.setText(MDetect.getText(name))
        event_edit_time.text = time
        event_edit_place.setText(MDetect.getText(place))
        event_edit_remainder.text = stralarm


        //declare variables
        lateinit var dialog:View
        val dateFormat = SimpleDateFormat("yyyy-M-dd")
        val event_day_calendar = Calendar.getInstance()
        val event_remainder_calendar = Calendar.getInstance()
        event_day_calendar.timeInMillis = event_day
        val year = event_day_calendar.get(Calendar.YEAR)
        val month = event_day_calendar.get(Calendar.MONTH)
        val day = event_day_calendar.get(Calendar.DAY_OF_MONTH)
        event_remainder_calendar.set(Calendar.YEAR,year)
        event_remainder_calendar.set(Calendar.MONTH,month)
        event_remainder_calendar.set(Calendar.DAY_OF_MONTH,day)





        //calendar date change
        event_edit_calendar.setOnDateChangeListener { calendarView, year, month, day ->
            event_day_calendar.set(Calendar.YEAR, year )
            event_day_calendar.set(Calendar.MONTH, month)
            event_day_calendar.set(Calendar.DAY_OF_MONTH, day)

            event_remainder_calendar.set(Calendar.YEAR,year)
            event_remainder_calendar.set(Calendar.MONTH,month)
            event_remainder_calendar.set(Calendar.DAY_OF_MONTH,day)

            clear_alarm()
        }


        //event edit time
        event_edit_time.setOnClickListener {

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
                event_edit_time.text = EventUtils.showTime(event_day_calendar)
                event_edit_time.textColor = Color.BLACK
                alertDialog.dismiss() }
        }

        //clear remainder
        event_remainder_clear.setOnClickListener {
            clear_alarm()
        }

        event_edit_remainder.setOnClickListener {

             if(event_day_calendar.timeInMillis  < Calendar.getInstance().timeInMillis)
                EventUtils.expired_time(context!!)

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
                    days.add(MDetect.getText("${Utils.myanNum(day.toString())}ရက်အလို"))
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

                dialog.event_remainder_save.setOnClickListener {

                    if(event_remainder_calendar.timeInMillis < Calendar.getInstance().timeInMillis)
                        EventUtils.expired_alarm(context!!)

                    else{
                        val namedMonth= event_remainder_calendar.getDisplayName(
                            Calendar.MONTH, Calendar.LONG, Locale.getDefault())

                        val show_remaind_day = "${event_remainder_calendar.get(Calendar.DAY_OF_MONTH)}-" +
                                "${event_remainder_calendar.get(Calendar.MONTH)+1}-" +
                                "${event_remainder_calendar.get(Calendar.YEAR)}"

                        event_edit_remainder.text = "$show_remaind_day ($namedMonth - ${EventUtils.showTime(event_remainder_calendar)})"
                        event_edit_remainder.textColor = Color.BLACK


                        event_remainder_clear.visibility = View.VISIBLE
                        check_alarm_time = false
                        alertDialog.dismiss()

                    }
                }
            }
        }

        event_edit_cancel.setOnClickListener {
            activity!!.onBackPressed()
        }
        event_edit_save.setOnClickListener {

            //save name and place
            val save_name =Utils.roomText(event_edit_name.text.toString())
            val save_place = Utils.roomText(event_edit_place.text.toString())


            //save date
            val save_date_year = Utils.myanNum(event_day_calendar.get(Calendar.YEAR).toString())
            val save_date_month =
                Utils.myanNum(event_day_calendar.get(Calendar.MONTH + 1).toString())
            val save_date_day =
                Utils.myanNum(event_day_calendar.get(Calendar.DAY_OF_MONTH).toString())
            val save_date = "$save_date_day.$save_date_month.$save_date_year"


            //get mm day
            val namedDay = event_remainder_calendar.get(Calendar.DAY_OF_WEEK)
            val save_time_day = EventUtils.mDay(namedDay.toString())

            //get mm titme
            val divided_time = event_day_calendar.get(Calendar.HOUR_OF_DAY)
            val save_mm_time = MDetect.getText(EventUtils.mAMPM(divided_time))


            //get hour
            val hour = event_day_calendar.get(Calendar.HOUR_OF_DAY)
            var formatted_hour = if(hour > 12) hour - 12  else hour

            //get minute
            val minute = event_day_calendar.get(Calendar.MINUTE)
            val formatted_minute = if( minute < 10) "0"+ minute else minute.toString()


            //save mmtime
            val save_time = "$save_time_day ( $save_mm_time - ${Utils.myanNum(formatted_hour.toString())} : ${Utils.myanNum(formatted_minute)} ) နာရီ"


            //save month name
            val namedMonth= event_remainder_calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())


            //save remaind day
            val save_remaind_day = "${event_remainder_calendar.get(Calendar.DAY_OF_MONTH)}-" +
                    "${event_remainder_calendar.get(Calendar.MONTH)+1}-" +
                    "${event_remainder_calendar.get(Calendar.YEAR)}"

            var save_str_alarm = "$save_remaind_day ($namedMonth - ${EventUtils.showTime(event_remainder_calendar)})"


            if(save_name.isBlank()){
                event_edit_name.hint = MDetect.getText("စာပေအခမ်းအနားအမည် သတ်မှတ်ပေးပါ")
                event_name.hintTextColor = Color.RED }


            else if(save_place.isBlank()){
                event_edit_place.hint = MDetect.getText("နေရာသတ်မှတ်ပေးပါ")
                event_place.hintTextColor = Color.RED }

            else{
//                if(check_alarm_time)
//                    save_str_alarm = MDetect.getText("ကြိုတင်အသိပေးချိန်သတ်မှတ်ထားခြင်းမရှိပါ")

                //no check conflict (event like DevCon)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    save_edit_name = save_name
                    save_edit_event_day = event_day_calendar.timeInMillis
                    save_edit_time = roomText( EventUtils.showTime(event_day_calendar))
                    save_edit_mm_time = save_time
                    save_edit_date = save_date
                    save_edit_place = save_place
                    save_edit_stralarm = save_str_alarm
                    save_edit_mlsalarm = event_remainder_calendar.timeInMillis
                    val event = Event(id = args.id,name = save_edit_name, day = save_edit_event_day,time =save_edit_time, mmtime =save_edit_mm_time , date = save_edit_date ,place = save_edit_place,strAlarm = save_edit_stralarm, mlsAlarm = save_edit_mlsalarm,setAlarm = false)
                    db.eventDao().updateEvent(event)
//
                }
                activity!!.onBackPressed()

            }
        }


    }

    fun clear_alarm(){
        check_alarm_time = true
        event_remainder_clear.visibility = View.GONE
        event_edit_remainder.text = MDetect.getText("ကြိုတင်အသိပေးချိန် သတ်မှတ်မည်")
        event_edit_remainder.textColor = Color.GRAY
    }

}
