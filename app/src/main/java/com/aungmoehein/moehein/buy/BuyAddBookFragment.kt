package com.aungmoehein.moehein.buy


import android.graphics.Color
import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.Buy
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.android.synthetic.main.fragment_add_buy_book.*
import kotlinx.android.synthetic.main.fragment_review_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit
import org.jetbrains.anko.hintTextColor
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BuyAddBookFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_buy_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ZawNi
        fun roomText(text:String): String {
            if(MDetect.isUnicode()){
                return text
            }
            return Rabbit.zg2uni(text)
        }

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val db = MoeHein.getInstance(context!!)
            val writer = MDetect.getStringArray(db.buyDao().getSugWriter())
            val qty = db.buyDao().getSugQty()
            async(Dispatchers.Main){
                val writerAdapter = ArrayAdapter<String>(context!!,
                    android.R.layout.simple_dropdown_item_1line,writer)
                buy_writer.setAdapter(writerAdapter)
                buy_writer.threshold = 1

                val qtyAdapter = ArrayAdapter<Long>(context!!,
                    android.R.layout.simple_dropdown_item_1line,qty)
                buy_quantity.setAdapter(qtyAdapter)
                buy_quantity.threshold = 1
            }

            buy_writer.onFocusChangeListener = View.OnFocusChangeListener{
                view,b ->
                if(b)
                    buy_writer.showDropDown()
            }
            buy_quantity.onFocusChangeListener = View.OnFocusChangeListener{
                    view,b ->
                if(b)
                    buy_quantity.showDropDown()
            }
        }

        buy_title.hint = MDetect.getText("စာအုပ်အမည်")
        buy_writer.hint = MDetect.getText("စာရေးသူ")
        buy_quantity.hint = MDetect.getText("အရေအတွက်")
        buy_comment.hint = MDetect.getText("မှတ်ချက်")

        save_buy.text = MDetect.getText("သိမ်းမည်")
        cancel_buy.text = MDetect.getText("မသိမ်းတော့ပါ")



        save_buy.setOnClickListener {

            val add_title = roomText(buy_title.text.toString())
            val add_writer = roomText(buy_writer.text.toString())
            val add_quantity = roomText(buy_quantity.text.toString())
            val add_comment = roomText(buy_comment.text.toString())

            if(add_title.isEmpty()){
                buy_title.hint = MDetect.getText("စာအုပ်အမည်ရေးပါ")
                buy_title.hintTextColor = Color.RED
            }else if(add_writer.isEmpty()){
                buy_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ")
                buy_writer.hintTextColor = Color.RED
            }else if(add_quantity.isEmpty()){
                buy_quantity.hint = MDetect.getText("စာအုပ်အရေအတွက်ရေးပါ")
                buy_quantity.hintTextColor = Color.RED
            }
            else{
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val checkConflict = db.buyDao().checkBuyConflict(add_title)
                    if(checkConflict == null)
                        db.buyDao().insertBuy(Buy(title = add_title,writer = add_writer,quantity = add_quantity.toLong(),comment = add_comment))

                }
                activity!!.onBackPressed()
            }
        }

        //cancel buy
        cancel_buy.setOnClickListener {
            activity!!.onBackPressed()
        }


    }


}
