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
import com.aungmoehein.moehein.poem.PoemListFragmentDirections
import kotlinx.android.synthetic.main.pop_up_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect

class BuyBookListAdapter(context: Context):RecyclerView.Adapter<BuyBookListAdapter.BuyViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private var buy = emptyList<Buy>()
    val db = MoeHein.getInstance(context)
    val context = context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuyBookListAdapter.BuyViewHolder {
        return BuyViewHolder(layoutInflater.inflate(R.layout.buy_book_list_layout,parent,false),this)
    }

    override fun getItemCount(): Int {
        return buy.size
    }

    override fun onBindViewHolder(holder: BuyBookListAdapter.BuyViewHolder, position: Int) {
        holder.title.text = MDetect.getText(buy[position].title)
        holder.writer.text = MDetect.getText(buy[position].writer)

        holder.pop_up_button.setOnClickListener {
            val dialog = LayoutInflater.from(context).inflate(R.layout.pop_up_layout,null)
            val builder = AlertDialog.Builder(context)
                .setView(dialog)
            val alertDialog = builder.show()
            dialog.pop_up_delete.setOnClickListener {
                alertDialog.dismiss()
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    db.buyDao().deleteBuy(buy[position])
                }
            }


            dialog.pop_up_edit.setOnClickListener {
                val buyEdit = BuyFragmentDirections.buyEditAction(buy[position].id,buy[position].title,buy[position].writer,buy[position].quantity,buy[position].comment)
                Navigation.findNavController(holder.pop_up_button).navigate(buyEdit)
                alertDialog.dismiss()

            }
        }

    }

    internal  fun setBuy(buy: List<Buy>){
        this.buy = buy
        notifyDataSetChanged()
    }

    class BuyViewHolder(itemView:View,adapter: BuyBookListAdapter):RecyclerView.ViewHolder(itemView),View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        val title = itemView.findViewById<TextView>(R.id.title)
        val writer =itemView.findViewById<TextView>(R.id.writer)
        val pop_up_button = itemView.findViewById<ImageButton>(R.id.pop_up_btn)
        val buyListAdapter = adapter

        override fun onClick(v: View?) {
            val buy_details = BuyFragmentDirections.buyDetailAction(
                buyListAdapter.buy[adapterPosition].title,
                buyListAdapter.buy[adapterPosition].writer,
                buyListAdapter.buy[adapterPosition].quantity,
                buyListAdapter.buy[adapterPosition].comment
            )
            Navigation.findNavController(itemView).navigate(buy_details)
        }

    }
}