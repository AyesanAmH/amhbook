package com.aungmoehein.moehein.adapter

import android.content.Context
import android.util.Log.i
import android.view.*
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.DoubleTapDetector
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.Event
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import org.jetbrains.anko.MDPI
import kotlin.math.exp

class EventListAdapter(val context: Context) : RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {
    val db = MoeHein.getInstance(context)
    private var events = emptyList<Event>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventListAdapter.EventViewHolder {
        return EventViewHolder(LayoutInflater.from(context).inflate(R.layout.event_list_layout,parent,false),context)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: EventListAdapter.EventViewHolder, position: Int) {
        val position = events[position]
        holder.name.text = MDetect.getText(position.name)
        holder.date.text = MDetect.getText(position.date)
        holder.time.text = MDetect.getText(position.time)
        holder.place.text = MDetect.getText(position.place)
        holder.alarm.text = position.strAlarm

        val scope = CoroutineScope(Dispatchers.IO)
        holder.event_delete.setOnClickListener {
            scope.launch {
                db.eventDao().deleteEvent(position)
            }
        }



    }

    internal fun setEvent(event : List<Event>){
        this.events = event
        notifyDataSetChanged()
    }
    class EventViewHolder(itemView: View,val context: Context) : RecyclerView.ViewHolder(itemView),View.OnTouchListener {

        init { itemView.setOnTouchListener(this) }

        val name = itemView.findViewById<TextView>(R.id.event_name)
        val date = itemView.findViewById<TextView>(R.id.event_date)
        val time = itemView.findViewById<TextView>(R.id.event_time)
        val place = itemView.findViewById<TextView>(R.id.event_place)
        val alarm = itemView.findViewById<TextView>(R.id.event_alarm)

        val place_layout = itemView.findViewById<LinearLayout>(R.id.place_layout)
        val alarm_layout = itemView.findViewById<LinearLayout>(R.id.alarm_layout)
        val event_edit = itemView.findViewById<ImageButton>(R.id.event_edit)
        val event_delete = itemView.findViewById<ImageButton>(R.id.event_delete)


        //double touch
        val detector = GestureDetector(context, DoubleTapDetector(::expand))
        val touchListener = View.OnTouchListener { v, event ->
            detector.onTouchEvent(event)
        }
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            detector.onTouchEvent(event)
            return true
        }

        internal fun expand(){
            if(place_layout.visibility == View.GONE){
                place_layout.visibility = View.VISIBLE
                alarm_layout.visibility = View.VISIBLE
                event_edit.visibility = View.VISIBLE
                event_delete.visibility = View.VISIBLE }


            else{
                place_layout.visibility = View.GONE
                alarm_layout.visibility = View.GONE
                event_edit.visibility = View.GONE
                event_delete.visibility = View.GONE }
        }
        fun Group.setAllOnTouchlistener(listener: View.OnTouchListener?){
            referencedIds.forEach { id ->
                rootView.findViewById<View>(id).setOnTouchListener(listener)
            }
        }
    }
}