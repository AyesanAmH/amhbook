package com.aungmoehein.moehein.review


import android.graphics.Color
import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.adapter.ReviewTopAdapter
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Review
import com.aungmoehein.moehein.viewmodel.ReviewViewModel
import kotlinx.android.synthetic.main.fragment_buy_book.*
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.fragment_review_add.*
import kotlinx.android.synthetic.main.review_cat_layout.*
import kotlinx.android.synthetic.main.review_fav_layout.*
import kotlinx.android.synthetic.main.review_layout_recycler.*
import kotlinx.android.synthetic.main.review_name_layout.*
import kotlinx.android.synthetic.main.review_writer_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect

/**
 * A simple [Fragment] subclass.
 */
class ReviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        review_add.text = MDetect.getText("သုံးသပ်ချက်ရေးမည်")
        review_latest_books.text = MDetect.getText("နောက်ဆုံး")
        review_all.text = MDetect.getText("အားလုံး")
        review.text = MDetect.getText("သုံးသပ်ချက်များ")
        no_review_books.text = MDetect.getText("သုံးသပ်ထားချက်ရေးထားသောစာအုပ်မရှိသေးပါ")


        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val db = MoeHein.getInstance(context!!)
            val qty_name = myanNum(db.reviewDao().countName().toString())
            val qty_writer= myanNum(db.reviewDao().countWriter().count().toString())
            val qty_cat = myanNum(db.reviewDao().countCat().count().toString())
            val qty_fav = myanNum(db.reviewDao().countFav(true).toString())

            review_quantity_name.text = MDetect.getText("အရေအတွက် - $qty_name")
            review_quantity_writer.text = MDetect.getText("အရေအတွက် - $qty_writer")
            review_quantity_cat.text = MDetect.getText("အရေအတွက် - $qty_cat")
            review_quantity_fav.text = MDetect.getText("အရေအတွက် - $qty_fav")

        }

        review_type_name.text = MDetect.getText("စာအုပ်")
        review_type_writer.text = MDetect.getText("စာရေးသူ")
        review_type_cat.text = MDetect.getText("အမျိုးအစား")
        review_type_fav.text = MDetect.getText("နှစ်သက်သော")


        review_add.setOnClickListener {
            val reviewAdd = ReviewFragmentDirections.addAction()
            Navigation.findNavController(it).navigate(reviewAdd)
        }

        review_all.setOnClickListener {
            val reviewAll = ReviewFragmentDirections.latestAction()
            Navigation.findNavController(it).navigate(reviewAll)
        }

        val viewModel = ViewModelProviders.of(this).get(ReviewViewModel::class.java)
        val reviewAdapter = ReviewTopAdapter(context!!)
        recycler_latest_book.adapter = reviewAdapter
        recycler_latest_book.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        viewModel.allreviews.observe(this, Observer { reviews ->
            reviews?.let { reviewAdapter.setReview(it) }
            if(reviewAdapter.reviews.isNotEmpty()){
                review_latest.visibility = View.VISIBLE
                review_scroll.visibility = View.VISIBLE
                no_review_books.visibility = View.GONE
            }else{
                no_review_books.visibility = View.VISIBLE
                review_latest.visibility = View.GONE
                review_scroll.visibility = View.GONE
            }

        })

    }


    fun myanNum(num:String):String{
            val mNum = StringBuilder()
            num.forEach {
                when(it){
                    '0' -> mNum.append("၀")
                    '1' -> mNum.append("၁")
                    '2' -> mNum.append(("၂"))
                    '3' -> mNum.append(("၃"))
                    '4' -> mNum.append(("၄"))
                    '5' -> mNum.append(("၅"))
                    '6'-> mNum.append(("၆"))
                    '7' -> mNum.append(("၇"))
                    '8' -> mNum.append(("၈"))
                    '9' -> mNum.append(("၉"))
                }
            }

            return mNum.toString()

    }


}
