package com.aungmoehein.moehein.adapter

import android.content.Context
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Review
import com.aungmoehein.moehein.review.ReviewFragmentDirections
import com.aungmoehein.moehein.review.ReviewWriterFragmentDirections
import me.myatminsoe.mdetect.MDetect

class ReviewWriterAdapter(val context: Context):RecyclerView.Adapter<ReviewWriterAdapter.ReviewHoler>() {
    private var reviewWriter = emptyList<Review>()
    private var reviewWriterBook = emptyList<Review>()
    val db = MoeHein.getInstance(context)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewWriterAdapter.ReviewHoler {
        return ReviewHoler(LayoutInflater.from(context).inflate(R.layout.writer_other_list_layout,parent,false),this)
    }

    override fun getItemCount(): Int {
        return reviewWriter.size
    }

    override fun onBindViewHolder(holder: ReviewWriterAdapter.ReviewHoler, position: Int) {
        holder.writer.text = MDetect.getText(reviewWriter[position].writer)
        holder.more_btn.setOnClickListener {
            if(holder.writer_book_recycler.visibility == View.GONE)
                holder.writer_book_recycler.visibility = View.VISIBLE
            else
                holder.writer_book_recycler.visibility = View.GONE
            val books = mutableListOf<Review>()
            reviewWriterBook.forEach{
                if(it.writer == reviewWriter[position].writer)
                    books.add(it)
            }
            val adapter =
                ReviewBookAdapter(context, books)
            holder.writer_book_recycler.adapter = adapter
            holder.writer_book_recycler.layoutManager = LinearLayoutManager(context)
        }
    }

    internal fun setReveiw(review: List<Review>){
        reviewWriter = review
        notifyDataSetChanged()
    }

    internal fun setReviewBook(review: List<Review>){
        reviewWriterBook = review
        notifyDataSetChanged()
    }


    class ReviewHoler(itemView: View,val adapter: ReviewWriterAdapter):RecyclerView.ViewHolder(itemView),View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        val writer = itemView.findViewById<TextView>(R.id.writer_other_list_title)
        val more_btn = itemView.findViewById<ImageButton>(R.id.more_btn)
        val writer_book_recycler = itemView.findViewById<RecyclerView>(R.id.writer_other_recycler)


        override fun onClick(v: View?) {
//            val position = adapter.reviewWriterBook[adapterPosition]
//            val detail = ReviewWriterFragmentDirections.detailAction(position.id,
//                position.title,position.writer,position.cat,position.fav,position.review)
//            Navigation.findNavController(itemView).navigate(detail)
        }
    }
}