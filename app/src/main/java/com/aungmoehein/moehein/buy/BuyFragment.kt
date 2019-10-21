package com.aungmoehein.moehein.buy


import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.aungmoehein.moehein.AccountFragment
import com.aungmoehein.moehein.LibraryFragment

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.poem.PoemDetailFragment
import kotlinx.android.synthetic.main.fragment_buy.*

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
        adapter.addFragment(BuyBookFragment(),"Name")
        adapter.addFragment(LibraryFragment(),"Writer")
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)

        //add fragment
        addBuy.setOnClickListener {
           val addBu = BuyFragmentDirections.addBuy()
            Navigation.findNavController(it).navigate(addBu)
        }
    }

}
