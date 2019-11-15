package com.aungmoehein.moehein.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Review
import com.aungmoehein.moehein.review.ReviewFragmentDirections
import kotlinx.android.synthetic.main.review_latest_book.view.*
import me.myatminsoe.mdetect.MDetect

class ReviewTopAdapter(context: Context):RecyclerView.Adapter<ReviewTopAdapter.ReviewTopHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private val db = MoeHein.getInstance(context)
    var reviews = emptyList<Review>()
    val context = context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewTopAdapter.ReviewTopHolder {
        return ReviewTopHolder(layoutInflater.inflate(R.layout.review_latest_book,parent,false),this)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ReviewTopAdapter.ReviewTopHolder, position: Int) {
        holder.title.text = MDetect.getText(reviews[position].title)
        holder.writer.text = MDetect.getText(reviews[position].writer)
    }

    internal  fun  setReview(review: List<Review>){
        this.reviews = review
        notifyDataSetChanged()

    }
    class ReviewTopHolder(itemView:View,adapter: ReviewTopAdapter):RecyclerView.ViewHolder(itemView),View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {

        }

        val title  = itemView.findViewById<TextView>(R.id.review_latest_book_name)
        val writer = itemView.findViewById<TextView>(R.id.review_latest_writer_name)

    }
}