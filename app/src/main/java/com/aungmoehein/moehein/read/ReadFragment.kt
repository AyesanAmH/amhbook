package com.aungmoehein.moehein.read


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.aungmoehein.moehein.PagerAdapter

import com.aungmoehein.moehein.R
import kotlinx.android.synthetic.main.fragment_buy.*
import kotlinx.android.synthetic.main.fragment_buy.tab_layout
import kotlinx.android.synthetic.main.fragment_buy.view_pager
import kotlinx.android.synthetic.main.fragment_read.*
import me.myatminsoe.mdetect.MDetect

/**
 * A simple [Fragment] subclass.
 */
class ReadFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PagerAdapter(childFragmentManager)
        adapter.addFragment(ReadNameFragment(),MDetect.getText("စာအုပ်"))
        adapter.addFragment(ReadWriterFragment(),MDetect.getText("စာရေးသူ"))
        adapter.addFragment(ReadRecomFragment(),MDetect.getText("အကြံပြုသူ"))
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)


        addRead.setOnClickListener {
            val addRead = ReadFragmentDirections.addRead()
            Navigation.findNavController(it).navigate(addRead)
        }


    }


}
