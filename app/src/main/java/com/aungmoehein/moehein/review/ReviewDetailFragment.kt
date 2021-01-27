package com.aungmoehein.moehein.review


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Review
import kotlinx.android.synthetic.main.fragment_review_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect

/**
 * A simple [Fragment] subclass.
 */
class ReviewDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg = ReviewDetailFragmentArgs.fromBundle(arguments!!)
        val writer = arg.writer
        val cat = arg.cat


        review_detail_name.text = MDetect.getText(arg.name)
        review_detail_writer.text = MDetect.getText("စာရေးသူ - $writer")
        review_detail_cat.text = MDetect.getText("စာပေအမျိုးအစား - $cat")
        review_detail_context.text = MDetect.getText(arg.review)

        if (arg.fav)
            review_detail_fav.setImageResource(R.drawable.ic_favorite_24px)
        else
          review_detail_fav.setImageResource(R.drawable.icon_love_present)


        var fav = !arg.fav
        review_detail_fav.setOnClickListener {
            val db = MoeHein.getInstance(context!!)
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                db.reviewDao().updateReview(Review(arg.id,arg.name,arg.writer,
                    arg.cat,arg.review,fav))
            }

            if(fav) review_detail_fav.setImageResource(R.drawable.ic_favorite_24px)

            else review_detail_fav.setImageResource(R.drawable.icon_love_present)
            fav = !fav
        }

    }

}
