package com.aungmoehein.moehein.buy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.Buy
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.android.synthetic.main.buy_book_list_layout.*
import kotlinx.android.synthetic.main.buy_book_list_layout.writer
import kotlinx.android.synthetic.main.fragment_add_buy_book.*
import kotlinx.android.synthetic.main.fragment_poem_add.*
import kotlinx.android.synthetic.main.poem_list_layout.*
import kotlinx.android.synthetic.main.poem_list_layout.title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit

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

        //set hint
        buy_title.hint = MDetect.getText("အမည်")
        buy_writer.hint = MDetect.getText("စာရေးသူ")
        buy_quantity.hint = MDetect.getText("အရေအတွက်")
        buy_comment.hint = MDetect.getText("မှတ်ချက်")

        //Set Button Text
        save_buy.text = MDetect.getText("သိမ်းမည်")
        cancel_buy.text = MDetect.getText("မသိမ်းတော့ပါ")

        //save buy
        save_buy.setOnClickListener {
            val add_title = roomText(buy_title.text.toString())
            val add_writer = roomText(buy_writer.text.toString())
            val add_quantity = roomText(buy_quantity.text.toString())
            val add_comment = roomText(buy_comment.text.toString())

            if(add_title.equals("")){
                buy_title.hint = MDetect.getText("စာအုပ်အမည်ရေးပါ")
            }else if(add_writer.equals("")){
                buy_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ")
            }else if(add_quantity.equals("")){
                buy_quantity.hint = MDetect.getText("စာအုပ်အရေအတွက်ရေးပါ")
            }
            else{
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
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