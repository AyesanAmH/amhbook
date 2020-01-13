package com.aungmoehein.moehein.review


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.Utils
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Review
import kotlinx.android.synthetic.main.fragment_read_add.*
import kotlinx.android.synthetic.main.fragment_review_add.*
import kotlinx.android.synthetic.main.fragment_review_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import org.jetbrains.anko.hintTextColor

/**
 * A simple [Fragment] subclass.
 */
class ReviewEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg = ReviewEditFragmentArgs.fromBundle(arguments!!)

//        val scope = CoroutineScope(Dispatchers.IO)
//        scope.launch {
//            val db = MoeHein.getInstance(context!!)
//            val cats = MDetect.getStringArray(db.reviewDao().getAllCat())
//            val writer = MDetect.getStringArray(db.reviewDao().getSugWriter())
//
//
//            async(Dispatchers.Main) {
//                val adapter = ArrayAdapter<String>(
//                    context!!,
//                    android.R.layout.simple_dropdown_item_1line, cats
//                )
//                val writerAdapter = ArrayAdapter<String>(
//                    context!!,
//                    android.R.layout.simple_dropdown_item_1line, writer
//                )
//
//
//                review_edit_cat.setAdapter(adapter)
//                review_edit_cat.threshold = 1
//                review_edit_writer.setAdapter(writerAdapter)
//                review_edit_writer.threshold = 1
//            }
//
//
//            review_edit_cat.onFocusChangeListener =
//                View.OnFocusChangeListener { view, b -> if (b) review_cat.showDropDown() }
//            review_edit_writer.onFocusChangeListener =
//                View.OnFocusChangeListener { view, b -> if (b) review_writer.showDropDown() }
//
//        }


        review_edit_title.hint = MDetect.getText("စာအုပ်အမည်")
        review_edit_writer.hint = MDetect.getText("စာရေးသူ")
        review_edit_cat.hint = MDetect.getText("စာအုပ်အမျိုးအစား")
        review_edit_context.hint = MDetect.getText("သုံးသပ်ချက်")

        review_edit_title.setText(MDetect.getText(arg.name))
        review_edit_writer.setText(MDetect.getText(arg.writer))
        review_edit_context.setText(MDetect.getText(arg.review))
        review_edit_cat.setText(MDetect.getText(arg.cat))
        fav_edit_check_box.isChecked = arg.fav

        fav_edit_check_box.text = MDetect.getText("နှစ်သက်သည့်စာရင်းတွင်ထည့်မည်")
        save_edit_review.text = MDetect.getText("ပြင်မည်")
        cancel_edit_review.text = MDetect.getText("မပြင်တော့ပါ")

        save_edit_review.setOnClickListener {
            val rtitle = Utils.roomText(review_edit_title.text.toString())
            val rwriter = Utils.roomText(review_edit_writer.text.toString())
            val rcat = Utils.roomText(review_edit_cat.text.toString())
            val rcontext = Utils.roomText(review_edit_context.text.toString())
            val fav = fav_edit_check_box.isChecked


            if (rtitle.isBlank()) {
                review_edit_title.hint = MDetect.getText("စာအုပ်အမည်ရေးပါ")
                review_edit_title.hintTextColor = Color.RED
            } else if (rwriter.isBlank()) {
                review_edit_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ")
                review_edit_writer.hintTextColor = Color.RED
            } else if (rcat.isBlank()) {
                review_edit_cat.hint = MDetect.getText("စာအုပ်အမျိုးအစားရေးပါ")
                review_edit_cat.hintTextColor = Color.RED
            } else if (rcontext.isBlank()) {
                review_edit_context.hint = MDetect.getText("သုံးသပ်ချက်ရေးပါ")
                review_edit_context.hintTextColor = Color.RED
            } else {
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                        db.reviewDao().updateReview(
                            Review(id = arg.id,
                                title = rtitle,
                                writer = rwriter, cat = rcat, review = rcontext, fav = fav
                            )
                        )
                }
                activity!!.onBackPressed()
            }
        }

        cancel_edit_review.setOnClickListener { activity!!.onBackPressed() }
    }


}


