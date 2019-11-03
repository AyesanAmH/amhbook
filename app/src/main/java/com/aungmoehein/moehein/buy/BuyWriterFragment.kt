package com.aungmoehein.moehein.buy


import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.adapter.BuyBookWriterAdapter
import com.aungmoehein.moehein.adapter.BuyWriterBookAdapter
import com.aungmoehein.moehein.db.Buy
import com.aungmoehein.moehein.viewmodel.BuyViewModel
import kotlinx.android.synthetic.main.buy_book_writer_list_layout.*
import kotlinx.android.synthetic.main.fragment_buy_book.*

/**
 * A simple [Fragment] subclass.
 */
class BuyWriterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_writer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //recycler
        val viewModel = ViewModelProviders.of(this).get(BuyViewModel::class.java)
        val buyBookWriterAdapter = BuyBookWriterAdapter(context!!)
        recycler.adapter = buyBookWriterAdapter
        recycler.layoutManager = LinearLayoutManager(context)


        viewModel.allbuywriter.observe(this, Observer { buywriter ->
            buywriter?.let { buyBookWriterAdapter.setBuyWriter(it) }
        })

        viewModel.allbuywriterbooks.observe(this, Observer { buywriterbooks ->
            buywriterbooks?.let { buyBookWriterAdapter.setWriterBook(it) }
        })

    }


}
