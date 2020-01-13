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
import com.aungmoehein.moehein.db.Review
import com.aungmoehein.moehein.review.ReviewFragmentDirections
import com.aungmoehein.moehein.review.ReviewNameFragmentDirections
import kotlinx.android.synthetic.main.pop_up_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import org.jetbrains.anko.MDPI

class ReviewNameAdapter(val context: Context):RecyclerView.Adapter<ReviewNameAdapter.ReviewHolder>() {
    val db = MoeHein.getInstance(context)
    private var review = emptyList<Review>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewNameAdapter.ReviewHolder {
        return ReviewHolder(LayoutInflater.from(context).inflate(R.layout.name_list_layout,parent,false),this)
    }

    override fun getItemCount(): Int { return review.size}

    override fun onBindViewHolder(holder: ReviewNameAdapter.ReviewHolder, position: Int) {
        holder.name.text = MDetect.getText(review[position].title)

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
                    db.reviewDao().deleteReview(review[position]) }
            }

            dialog.pop_up_edit.setOnClickListener {
                val edit = ReviewNameFragmentDirections.editAction(review[position].id,review[position].title,review[position].writer,review[position].cat,review[position].review,review[position].fav)
                Navigation.findNavController(holder.pop_up_button).navigate(edit)
                alertDialog.dismiss()
            }
        }
    }

    internal fun setReview(review:List<Review>){
        this.review = review
        notifyDataSetChanged() }

    class ReviewHolder(itemView: View,val adapter: ReviewNameAdapter ):RecyclerView.ViewHolder(itemView),View.OnClickListener{

        init { itemView.setOnClickListener(this)}

        val name = itemView.findViewById<TextView>(R.id.title)
        val pop_up_button = itemView.findViewById<ImageButton>(R.id.pop_up_btn)

        override fun onClick(v: View?) {
            val position = adapter.review[adapterPosition]
            val detail = ReviewNameFragmentDirections.detailAction(position.id,
                position.title,position.writer,position.cat,position.fav,position.review)
            Navigation.findNavController(itemView).navigate(detail)
        }

    }
}