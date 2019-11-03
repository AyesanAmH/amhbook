package com.aungmoehein.moehein.adapter

import android.content.Context
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.Buy
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.android.synthetic.main.buy_book_list_layout.*
import kotlinx.android.synthetic.main.buy_book_list_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.myatminsoe.mdetect.MDetect

class BuyBookWriterAdapter(context:Context):RecyclerView.Adapter<BuyBookWriterAdapter.BuyWriterViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private var buyWriter =  emptyList<Buy>()
    private var buyWriterBooks = emptyList<Buy>()
    private var books = mutableListOf<Buy>()
    val db = MoeHein.getInstance(context)
    val context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyBookWriterAdapter.BuyWriterViewHolder {
        return BuyWriterViewHolder(layoutInflater.inflate(R.layout.buy_book_writer_list_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return buyWriter.size
    }

    override fun onBindViewHolder(holder: BuyBookWriterAdapter.BuyWriterViewHolder, position: Int) {
        holder.title.text = MDetect.getText(buyWriter[position].writer)















        holder.more_btn.setOnClickListener {
            if(holder.writer_book_recycler.visibility == View.GONE)
            holder.writer_book_recycler.visibility = View.VISIBLE
            else
                holder.writer_book_recycler.visibility = View.GONE


            var buyBooks = mutableListOf<Buy>()
            buyWriterBooks.forEach{
                if(it.writer == buyWriter[position].writer){
                    buyBooks.add(it)
                }
            }
            books = buyBooks
            i("books",books.toString())
            val adapter = BuyWriterBookAdapter(context,books)
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
        val title = itemView.findViewById<TextView>(R.id.buy_writer_title)
        val more_btn = itemView.findViewById<ImageButton>(R.id.more_btn)
        val writer_book_recycler = itemView.findViewById<RecyclerView>(R.id.buy_writer_book_recycler)


    }
}