package com.aungmoehein.moehein.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.Utils.myanNum
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

class BuyNameAdapter(val context: Context):RecyclerView.Adapter<BuyNameAdapter.BuyViewHolder>() {
    val db = MoeHein.getInstance(context)
    private var buy = emptyList<Buy>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuyNameAdapter.BuyViewHolder {
        return BuyViewHolder(LayoutInflater.from(context).inflate(R.layout.name_list_layout,parent,false),this,context)
    }

    override fun getItemCount(): Int { return buy.size }

    override fun onBindViewHolder(holder: BuyNameAdapter.BuyViewHolder, position: Int) {
        holder.title.text = MDetect.getText(buy[position].title)

        holder.pop_up_button.setOnClickListener {
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

    class BuyViewHolder(itemView:View,val adapter: BuyNameAdapter,val context: Context):RecyclerView.ViewHolder(itemView),View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        val title = itemView.findViewById<TextView>(R.id.title)
        val pop_up_button = itemView.findViewById<ImageButton>(R.id.pop_up_btn)

        override fun onClick(v: View?) {
            val dialog = LayoutInflater.from(context).inflate(R.layout.details_buy_layout,null)
            val builder = AlertDialog.Builder(context).setView(dialog)
            builder.show()
            val position = adapter.buy[adapterPosition]
            dialog.details_buy_title.text = MDetect.getText("စာအုပ်အမည် - ${position.title}")
            dialog.details_buy_writer.text = MDetect.getText("စာရေးသူ - ${position.writer}")
            dialog.details_buy_qty.text = MDetect.getText("အရေအတွက် - ${myanNum(position.quantity.toString())}အုပ်")
            if (adapter.buy[adapterPosition].comment.isNotEmpty()){
                dialog.details_buy_comment.visibility = View.VISIBLE
                dialog.details_buy_comment.text = MDetect.getText("မှတ်ချက်  ။      ။\n${position.comment}")
            }
        }

    }
}