package com.aungmoehein.moehein.buy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.adapter.BuyNameAdapter
import com.aungmoehein.moehein.viewmodel.BuyViewModel
import kotlinx.android.synthetic.main.fragment_buy_book.*

/**
 * A simple [Fragment] subclass.
 */
class   BuyNameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(BuyViewModel::class.java)
        val buyBookListAdapter = BuyNameAdapter(context!!)
        recycler.adapter = buyBookListAdapter
        recycler.layoutManager = LinearLayoutManager(context)
        viewModel.allbuy.observe(viewLifecycleOwner, Observer {buy ->
            buy?.let { buyBookListAdapter.setBuy(it) } })
    }


}
