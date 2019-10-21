package com.aungmoehein.moehein.buy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aungmoehein.moehein.R
import kotlinx.android.synthetic.main.fragment_buy_details.*
import me.myatminsoe.mdetect.MDetect

/**
 * A simple [Fragment] subclass.
 */
class BuyDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btitle = BuyDetailsFragmentArgs.fromBundle(arguments!!).title
        val bwriter = BuyDetailsFragmentArgs.fromBundle(arguments!!).writer
        val bquantity = BuyDetailsFragmentArgs.fromBundle(arguments!!).quantity
        val bcomment = BuyDetailsFragmentArgs.fromBundle(arguments!!).comment

        buy_detail_title.text = MDetect.getText(btitle)
        buy_detail_writer.text = MDetect.getText(bwriter)
        buy_detail_quantity.text = MDetect.getText(bquantity.toString())
        buy_detail_comment.text = MDetect.getText(bcomment)

    }


}
