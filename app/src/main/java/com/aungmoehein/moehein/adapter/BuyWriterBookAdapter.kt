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
import com.aungmoehein.moehein.db.Buy
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.review.ReviewFragment
import kotlinx.android.synthetic.main.details_buy_layout.view.*
import kotlinx.android.synthetic.main.pop_up_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect

class BuyWriterBookAdapter(context: Context, buywriterbooklist: List<Buy>): RecyclerView.Adapter<BuyWriterBookAdapter.BuyWriterBookViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    val db = MoeHein.getInstance(context)
    private val booklist = buywriterbooklist
    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyWriterBookAdapter.BuyWriterBookViewHolder {
        return BuyWriterBookViewHolder(layoutInflater.inflate(R.layout.writer_other_list,parent,false),this,context)
    }

    override fun getItemCount(): Int {
        return booklist.size
    }

    override fun onBindViewHolder(holder: BuyWriterBookAdapter.BuyWriterBookViewHolder, position: Int) {
        holder.title.text = MDetect.getText(booklist[position].title)

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
                    db.buyDao().deleteBuy(booklist[position])
                }
            }

            dialog.pop_up_edit.setOnClickListener {
                val buyEdit = BuyFragmentDirections.buyEditAction(booklist[position].id,booklist[position].title,booklist[position].writer,booklist[position].quantity,booklist[position].comment)
                Navigation.findNavController(holder.pop_up_btn).navigate(buyEdit)
                alertDialog.dismiss()

            }
        }
    }



    class BuyWriterBookViewHolder(itemView: View,adapter: BuyWriterBookAdapter,val context: Context) : RecyclerView.ViewHolder(itemView),View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        val title = itemView.findViewById<TextView>(R.id.writer_other_title)
        val pop_up_btn = itemView.findViewById<ImageButton>(R.id.pop_up_btn)
        val adapter = adapter

        override fun onClick(v: View?) {
            val dialog = LayoutInflater.from(context).inflate(R.layout.details_buy_layout,null)
            val builder = AlertDialog.Builder(context).setView(dialog)
            builder.show()
            dialog.details_buy_title.text = MDetect.getText("စာအုပ်အမည် - "+adapter.booklist[adapterPosition].title)
            dialog.details_buy_writer.text = MDetect.getText("စာရေးသူ - "+adapter.booklist[adapterPosition].writer)
            dialog.details_buy_qty.text = MDetect.getText("အရေအတွက် - "+ ReviewFragment().myanNum(adapter.booklist[adapterPosition].quantity.toString())+"အုပ်")
            if (adapter.booklist[adapterPosition].comment.isNotEmpty()){
                dialog.details_buy_comment.visibility = View.VISIBLE
                dialog.details_buy_comment.text = MDetect.getText("မှတ်ချက်  ။      ။\n"+adapter.booklist[adapterPosition].comment)
            }

        }
    }
}