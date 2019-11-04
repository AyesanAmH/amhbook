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
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Read
import com.aungmoehein.moehein.read.ReadFragmentDirections
import kotlinx.android.synthetic.main.pop_up_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect

class ReadWriterRecomBookAdapter(context: Context, readList: List<Read>):RecyclerView.Adapter<ReadWriterRecomBookAdapter.ReadWriterViewHolder>(){
    private val layoutInflater = LayoutInflater.from(context)
    val db = MoeHein.getInstance(context)
    val context = context
    val read = readList


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReadWriterRecomBookAdapter.ReadWriterViewHolder {
        return ReadWriterViewHolder(layoutInflater.inflate(R.layout.writer_other_list,parent,false),this)
    }

    override fun getItemCount(): Int {
        return read.size
    }

    override fun onBindViewHolder(
        holder: ReadWriterRecomBookAdapter.ReadWriterViewHolder,
        position: Int
    ) {
        holder.title.text = MDetect.getText(read[position].title)

        holder.pop_up_btn.setOnClickListener {
            val dialog = LayoutInflater.from(context).inflate(R.layout.pop_up_layout,null)
            val builder = AlertDialog.Builder(context)
                .setView(dialog)
            val alertDialog = builder.show()
            dialog.pop_up_delete.setOnClickListener {
                alertDialog.dismiss()
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    db.readDao().deleteRead(read[position])
                }
            }

            dialog.pop_up_edit.setOnClickListener {
                val redit = ReadFragmentDirections.editAction(read[position].id,read[position].title,read[position].writer,read[position].recom,read[position].comment)
                Navigation.findNavController(holder.pop_up_btn).navigate(redit)
                alertDialog.dismiss()

            }
        }
    }


    class ReadWriterViewHolder(itemView : View, adapter: ReadWriterRecomBookAdapter):RecyclerView.ViewHolder(itemView),View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        val title = itemView.findViewById<TextView>(R.id.writer_other_title)
        val pop_up_btn = itemView.findViewById<ImageButton>(R.id.pop_up_btn)
        val adapter = adapter


        override fun onClick(v: View?) {
            val readDetails = ReadFragmentDirections.detailsAction(
                adapter.read[adapterPosition].title,
                adapter.read[adapterPosition].writer,
                adapter.read[adapterPosition].recom,
                adapter.read[adapterPosition].comment
            )
            Navigation.findNavController(itemView).navigate(readDetails)
        }

    }
}