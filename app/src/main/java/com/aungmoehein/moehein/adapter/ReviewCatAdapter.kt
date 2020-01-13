package com.aungmoehein.moehein.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Review
import me.myatminsoe.mdetect.MDetect

class ReviewCatAdapter(val context: Context):RecyclerView.Adapter<ReviewCatAdapter.ReviewHoler>() {
    private var reviewCat = emptyList<Review>()
    private var reviewBook = emptyList<Review>()
    val db = MoeHein.getInstance(context)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewCatAdapter.ReviewHoler {
        return ReviewHoler(LayoutInflater.from(context).inflate(R.layout.writer_other_list_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return reviewCat.size
    }

    override fun onBindViewHolder(holder: ReviewCatAdapter.ReviewHoler, position: Int) {
        holder.cat.text = MDetect.getText(reviewCat[position].cat)
        holder.more_btn.setOnClickListener {
            if(holder.writer_book_recycler.visibility == View.GONE)
                holder.writer_book_recycler.visibility = View.VISIBLE
            else
                holder.writer_book_recycler.visibility = View.GONE
            val books = mutableListOf<Review>()
            reviewBook.forEach{
                if(it.writer == reviewCat[position].writer)
                    books.add(it)
            }
            val adapter = ReviewBookAdapter(context, books)
            holder.writer_book_recycler.adapter = adapter
            holder.writer_book_recycler.layoutManager = LinearLayoutManager(context)
        }
    }

    internal fun setCat(review:List<Review>){
        reviewCat = review
        notifyDataSetChanged() }

    internal fun setBook(review: List<Review>){
        reviewBook = review
        notifyDataSetChanged() }

    class ReviewHoler(itemView: View):RecyclerView.ViewHolder(itemView) {


        val cat = itemView.findViewById<TextView>(R.id.writer_other_list_title)
        val more_btn = itemView.findViewById<ImageButton>(R.id.more_btn)
        val writer_book_recycler = itemView.findViewById<RecyclerView>(R.id.writer_other_recycler)


    }

}