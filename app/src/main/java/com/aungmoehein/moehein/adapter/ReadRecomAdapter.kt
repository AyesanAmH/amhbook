package com.aungmoehein.moehein.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Read
import me.myatminsoe.mdetect.MDetect

class ReadRecomAdapter(val context:Context):RecyclerView.Adapter<ReadRecomAdapter.ReadRecomViewHolder>() {
    private var readRecom =  emptyList<Read>()
    private var readBooks = emptyList<Read>()
    val db = MoeHein.getInstance(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReadRecomAdapter.ReadRecomViewHolder {
        return ReadRecomViewHolder( LayoutInflater.from(context).inflate(
            R.layout.writer_other_list_layout,
            parent,
            false)
        )
    }

    override fun getItemCount(): Int {
        return readRecom.size
    }

    override fun onBindViewHolder(holder: ReadRecomAdapter.ReadRecomViewHolder, position: Int) {

        val recom = readRecom[position].recom


        if(recom.isNotBlank()){
            holder.recom_list.visibility = View.VISIBLE

            holder.title.text = MDetect.getText(recom)

            holder.more_btn.setOnClickListener {
                if (holder.writer_book_recycler.visibility == View.GONE)
                    holder.writer_book_recycler.visibility = View.VISIBLE
                else
                    holder.writer_book_recycler.visibility = View.GONE

                var read= mutableListOf<Read>()

                readBooks.forEach {
                    if(it.recom == readRecom[position].recom)
                        read.add(it)
                }

                val adapter = ReadBookAdapter(context, read)
                holder.writer_book_recycler?.adapter = adapter
                holder.writer_book_recycler?.layoutManager = LinearLayoutManager(context)
            }
        }

        else{
            holder.recom_list.visibility = View.GONE
        }

    }
    internal fun setRecomWriter(readWriter:List<Read>){
        this.readRecom = readWriter
        notifyDataSetChanged()
    }

    internal fun setReadWriterBooks(readWriterBook:List<Read>){
        this.readBooks = readWriterBook
        notifyDataSetChanged()
    }
    class ReadRecomViewHolder(itemView :View):RecyclerView.ViewHolder(itemView)
    {
        val title = itemView.findViewById<TextView>(R.id.writer_other_list_title)
        val more_btn = itemView.findViewById<ImageButton>(R.id.more_btn)
        val writer_book_recycler = itemView.findViewById<RecyclerView>(R.id.writer_other_recycler)
        val recom_list = itemView.findViewById<LinearLayout>(R.id.recom_list)
    }
}