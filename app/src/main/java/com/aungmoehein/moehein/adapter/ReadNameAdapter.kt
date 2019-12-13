package com.aungmoehein.moehein.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.buy.BuyFragmentDirections
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Read
import com.aungmoehein.moehein.read.ReadFragmentDirections
import kotlinx.android.synthetic.main.details_read_layout.view.*
import kotlinx.android.synthetic.main.name_list_layout.view.*
import kotlinx.android.synthetic.main.pop_up_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect

class ReadNameAdapter(val context: Context):RecyclerView.Adapter<ReadNameAdapter.ReadNameViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    val db = MoeHein.getInstance(context)
    private var read = emptyList<Read>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReadNameAdapter.ReadNameViewHolder {
        return ReadNameViewHolder(layoutInflater.inflate(R.layout.name_list_layout,parent,false),this,context)
    }

    override fun getItemCount(): Int {
        return read.size
    }

    override fun onBindViewHolder(holder: ReadNameAdapter.ReadNameViewHolder, position: Int) {
        holder.title.text = MDetect.getText(read[position].title)

        holder.pop_up_btn.setOnClickListener {
            val dialog = LayoutInflater.from(context).inflate(R.layout.pop_up_layout,null)
            val builder = AlertDialog.Builder(context)
                .setView(dialog)
            val alertDialog = builder.show()
            dialog.pop_up_edit.text = MDetect.getText("ပြင်မည်")
            dialog.pop_up_delete.text = MDetect.getText("ဖျက်မည်")
            dialog.pop_up_delete.setOnClickListener {
                alertDialog.dismiss()
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    db.readDao().deleteRead(read[position])
                }
            }


            dialog.pop_up_edit.setOnClickListener {
                val readEdit = ReadFragmentDirections.editAction(read[position].id,read[position].title,read[position].writer,read[position].recom,read[position].comment)
                Navigation.findNavController(holder.pop_up_btn).navigate(readEdit)
                alertDialog.dismiss()

            }
        }
    }

    internal fun setRead(read:List<Read>){
        this.read = read
        notifyDataSetChanged()
    }

    class ReadNameViewHolder(itemView: View, adapter: ReadNameAdapter,val context: Context):RecyclerView.ViewHolder(itemView),View.OnClickListener
    {
        init {
            itemView.setOnClickListener(this)
        }

        val title = itemView.findViewById<TextView>(R.id.title)
        val pop_up_btn = itemView.findViewById<ImageButton>(R.id.pop_up_btn)
        val adapter = adapter
        override fun onClick(v: View?) {
            val dialog = LayoutInflater.from(context).inflate(R.layout.details_read_layout,null)
            val builder = AlertDialog.Builder(context).setView(dialog)
            builder.show()
            val position = adapter.read[adapterPosition]
            dialog.details_read_title.text = MDetect.getText("စာအုပ်အမည် - ${position.title}" )
            dialog.details_read_writer.text = MDetect.getText("စာရေးသူ - ${position.writer}")
            if(position.recom.isEmpty())
                dialog.details_read_recom.visibility = View.GONE
            dialog.details_read_recom.text = MDetect.getText("အကြံပြုသူ - ${position.recom}")
            if (position.comment.isEmpty())
                dialog.details_read_comment.visibility = View.GONE
            dialog.details_read_comment.text = MDetect.getText("မှတ်ချက်  ။      ။\n${position.comment}")

        }

    }
}