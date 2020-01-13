package com.aungmoehein.moehein.adapter

import android.content.Context
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.Buy
import com.aungmoehein.moehein.db.MoeHein
import me.myatminsoe.mdetect.MDetect

class BuyWriterAdapter(val context:Context):RecyclerView.Adapter<BuyWriterAdapter.BuyWriterViewHolder>() {
    private var buyWriter =  emptyList<Buy>()
    private var buyWriterBooks = emptyList<Buy>()
    val db = MoeHein.getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyWriterAdapter.BuyWriterViewHolder {
        return BuyWriterViewHolder(LayoutInflater.from(context).inflate(R.layout.writer_other_list_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return buyWriter.size
    }

    override fun onBindViewHolder(holder: BuyWriterAdapter.BuyWriterViewHolder, position: Int) {
        holder.title.text = MDetect.getText(buyWriter[position].writer)
        holder.more_btn.setOnClickListener {
            if(holder.writer_book_recycler.visibility == View.GONE)
            holder.writer_book_recycler.visibility = View.VISIBLE
            else
                holder.writer_book_recycler.visibility = View.GONE


            val books = mutableListOf<Buy>()
            buyWriterBooks.forEach{
                if(it.writer == buyWriter[position].writer){
                    books.add(it)
                }
            }

            val adapter = BuyBookAdapter(context,books)
            holder.writer_book_recycler?.adapter = adapter
            holder.writer_book_recycler?.layoutManager = LinearLayoutManager(holder.writer_book_recycler.context,RecyclerView.VERTICAL,false)
        }

    }


    internal fun setBuyWriter (buyWriter :List<Buy>){
        this.buyWriter = buyWriter
        notifyDataSetChanged()
    }

    internal fun setWriterBook(writerBooks: List<Buy>){
        this.buyWriterBooks = writerBooks
        notifyDataSetChanged()
    }


    class BuyWriterViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.writer_other_list_title)
        val more_btn = itemView.findViewById<ImageButton>(R.id.more_btn)
        val writer_book_recycler = itemView.findViewById<RecyclerView>(R.id.writer_other_recycler)
    }



}