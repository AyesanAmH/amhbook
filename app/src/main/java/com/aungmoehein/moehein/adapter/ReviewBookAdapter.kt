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
import com.aungmoehein.moehein.db.Review
import com.aungmoehein.moehein.review.ReviewCatFragmentDirections
import com.aungmoehein.moehein.review.ReviewWriterFragmentDirections
import kotlinx.android.synthetic.main.pop_up_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect

class ReviewBookAdapter(val context: Context, val list: List<Review>):RecyclerView.Adapter<ReviewBookAdapter.ReviewHolder>(){
    val db = MoeHein.getInstance(context)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewHolder {
        return ReviewHolder(
            LayoutInflater.from(context).inflate(R.layout.writer_other_list, parent, false),
            this
        )
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.title.text = MDetect.getText(list[position].title)
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
                    db.reviewDao().deleteReview(list[position])
                }
            }


            dialog.pop_up_edit.setOnClickListener {
                val edit =
                    ReviewWriterFragmentDirections.editAction(
                        list[position].id,
                        list[position].title,
                        list[position].writer,
                        list[position].cat,
                        list[position].review,
                        list[position].fav
                    )
                Navigation.findNavController(holder.pop_up_btn).navigate(edit)
                alertDialog.dismiss()
            }
        }
    }

    class ReviewHolder(itemView: View,val adapter: ReviewBookAdapter):RecyclerView.ViewHolder(itemView),View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        val title = itemView.findViewById<TextView>(R.id.writer_other_title)
        val pop_up_btn = itemView.findViewById<ImageButton>(R.id.pop_up_btn)

        override fun onClick(v: View?) {
            val detail = ReviewCatFragmentDirections.detailAction(adapter.list[adapterPosition].id,
                adapter.list[adapterPosition].title,adapter.list[adapterPosition].writer,
                adapter.list[adapterPosition].cat,
                adapter.list[adapterPosition].fav,
                adapter.list[adapterPosition].review)
            Navigation.findNavController(itemView).navigate(detail)
        }

    }
}