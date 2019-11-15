package com.aungmoehein.moehein.buy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.aungmoehein.moehein.PagerAdapter

import com.aungmoehein.moehein.R
import kotlinx.android.synthetic.main.fragment_buy.*
import me.myatminsoe.mdetect.MDetect

/**
 * A simple [Fragment] subclass.
 */
class BuyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PagerAdapter(childFragmentManager)
        adapter.addFragment(BuyNameFragment(),MDetect.getText("စာအုပ်"))
        adapter.addFragment(BuyWriterFragment(),MDetect.getText("စာရေးသူ"))
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)

        //add fragment
        addBuy.setOnClickListener {
           val addBu = BuyFragmentDirections.addBuy()
            Navigation.findNavController(it).navigate(addBu)
        }
    }

}
