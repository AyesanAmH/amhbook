package com.aungmoehein.moehein.review


import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Read
import com.aungmoehein.moehein.db.Review
import com.aungmoehein.moehein.viewmodel.ReviewViewModel
import kotlinx.android.synthetic.main.fragment_read_add.*
import kotlinx.android.synthetic.main.fragment_review_add.*
import kotlinx.coroutines.*
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit
import java.lang.reflect.Array

/**
 * A simple [Fragment] subclass.
 */
class ReviewAddFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun roomText(text:String): String {
            if(MDetect.isUnicode()){
                return text
            }
            return Rabbit.zg2uni(text)
        }

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val db = MoeHein.getInstance(context!!)
            val cats = MDetect.getStringArray(db.reviewDao().getAllCat())
            val writer = MDetect.getStringArray(db.reviewDao().getSugWriter())
            async (Dispatchers.Main){
                val adapter =   ArrayAdapter<String>(context!!,
                    android.R.layout.simple_dropdown_item_1line,cats)
                review_cat.setAdapter(adapter)
                review_cat.threshold = 1

                val writerAdapter = ArrayAdapter<String>(context!!,
                    android.R.layout.simple_dropdown_item_1line,writer)
                review_writer.setAdapter(writerAdapter)
                review_writer.threshold = 1

            }
            review_cat.onFocusChangeListener = View.OnFocusChangeListener{
                view,b ->
                if(b)
                    review_cat.showDropDown()
            }
            review_writer.onFocusChangeListener = View.OnFocusChangeListener{
                    view,b ->
                if(b)
                    review_writer   .showDropDown()
            }
        }

        review_title.hint = MDetect.getText("စာအုပ်အမည်")
        review_writer.hint = MDetect.getText("စာရေးသူ")
        review_cat.hint = MDetect.getText("စာအုပ်အမျိုးအစား")
        review_context.hint = MDetect.getText("သုံးသပ်ချက်")
        fav_check_box.text = MDetect.getText("နှစ်သက်သည့်စာရင်းတွင်ထည့်မည်")
        save_review.text = MDetect.getText("သိမ်းမည်")
        cancel_review.text = MDetect.getText("မသိမ်းတော့ပါ")

        save_review.setOnClickListener {
            val rtitle = roomText(review_title.text.toString())
            val rwriter = roomText(review_writer.text.toString())
            val rcat = roomText(review_cat.text.toString())
            val rcontext = roomText(review_context.text.toString())
            val fav = fav_check_box.isChecked

            if(rtitle.isEmpty())
                review_title.hint = MDetect.getText("စာအုပ်အမည်ရေးပါ")
            else if (rwriter.isEmpty())
                read_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ")
            else if(rcat .isEmpty())
                review_cat.hint = MDetect.getText("စာအုပ်အမျိုးအစားရေးပါ")
            else if(rcontext .isEmpty())
                review_context.hint = MDetect.getText("သုံးသပ်ချက်ရေးပါ")
            else{
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val checkConflict = db.reviewDao().checkConflict(rtitle)
                   if(checkConflict == null)
                       db.reviewDao().insertReview(Review(title = rtitle,writer = rwriter,cat = rcat,review = rcontext,fav = fav))
                    val add = db.reviewDao().getAllCat()
                }
                activity!!.onBackPressed()
            }
        }



        cancel_review.setOnClickListener {
            activity!!.onBackPressed()
        }
    }

}
