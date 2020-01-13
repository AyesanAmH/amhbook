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

class ReadWriterAdapter(context: Context):RecyclerView.Adapter<ReadWriterAdapter.ReadWriterViewHolder>(){
    private val layoutInflater = LayoutInflater.from(context)
    private var readWriter =  emptyList<Read>()
    private var readBooks = emptyList<Read>()
    private var read = mutableListOf<Read>()
    val db = MoeHein.getInstance(context)
    val context = context


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReadWriterAdapter.ReadWriterViewHolder {
        return ReadWriterViewHolder(
            layoutInflater.inflate(
                R.layout.writer_other_list_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return readWriter.size
    }

    override fun onBindViewHolder(holder: ReadWriterAdapter.ReadWriterViewHolder, position: Int) {
        holder.title.text = MDetect.getText(readWriter[position].writer)
        holder.more_btn.setOnClickListener {

            if (holder.writer_book_recycler.visibility == View.GONE)
                holder.writer_book_recycler.visibility = View.VISIBLE
            else
                holder.writer_book_recycler.visibility = View.GONE

            var readBook= mutableListOf<Read>()


            readBooks.forEach {
                if(it.writer == readWriter[position].writer)
                        readBook.add(it)
            }

            read = readBook
            val adapter = ReadBookAdapter(context, read)
            holder.writer_book_recycler?.adapter = adapter
            holder.writer_book_recycler?.layoutManager = LinearLayoutManager(context)
        }

    }

    internal fun setReadWriter(readWriter:List<Read>){
        this.readWriter = readWriter
        notifyDataSetChanged()
    }

    internal fun setReadWriterBooks(readWriterBook:List<Read>){
        this.readBooks = readWriterBook
        notifyDataSetChanged()
    }
    class ReadWriterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.writer_other_list_title)
        val more_btn = itemView.findViewById<ImageButton>(R.id.more_btn)
        val writer_book_recycler = itemView.findViewById<RecyclerView>(R.id.writer_other_recycler)
        val read_list = itemView.findViewById<LinearLayout>(R.id.recom_list)

    }
}