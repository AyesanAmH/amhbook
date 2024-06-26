package com.aungmoehein.moehein.buy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.Utils.roomText
import com.aungmoehein.moehein.db.Buy
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.android.synthetic.main.fragment_add_buy_book.*
import kotlinx.android.synthetic.main.fragment_buy_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit

/**
 * A simple [Fragment] subclass.
 */
class BuyEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg = BuyEditFragmentArgs.fromBundle(arguments!!)

        buy_edit_title.hint = MDetect.getText("အမည်")
        buy_edit_writer.hint = MDetect.getText("စာရေးသူ")
        buy_edit_quantity.hint = MDetect.getText("အရေအတွက်")
        buy_edit_comment.hint = MDetect.getText("မှတ်ချက်")

        save_buy_edit.text = MDetect.getText("ပြင်မည်")
        cancel_buy_edit.text = MDetect.getText("မပြင်တော့ပါ")


        buy_edit_title.setText(MDetect.getText(arg.title))
        buy_edit_writer.setText(MDetect.getText(arg.writer))
        buy_edit_quantity.setText(MDetect.getText(arg.quantity.toString()))
        buy_edit_comment.setText(MDetect.getText(arg.comment))

        save_buy_edit.setOnClickListener {
            val add_title = roomText(buy_edit_title.text.toString())
            val add_writer = roomText(buy_edit_writer.text.toString())
            val add_quantity = roomText(buy_edit_quantity.text.toString())
            val add_comment = roomText(buy_edit_comment.text.toString())


            if(add_title.isBlank()){
                buy_edit_title.hint = MDetect.getText("စာအုပ်အမည်ရေးပါ") }

            else if(add_writer.isBlank()){
                buy_edit_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ") }

            else if(add_quantity.isBlank()){
                buy_edit_quantity.hint = MDetect.getText("စာအုပ်အရေအတွက်ရေးပါ") }

            else{
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val buy = Buy(id = arg.id,title = add_title,writer = add_writer,quantity = add_quantity.toLong(),comment = add_comment)
                    db.buyDao().updateBuy(buy)
                }
                activity!!.onBackPressed()
            }
        }

        cancel_buy_edit.setOnClickListener { activity!!.onBackPressed() }
    }


}
