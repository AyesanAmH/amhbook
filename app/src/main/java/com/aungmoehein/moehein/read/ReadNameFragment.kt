package com.aungmoehein.moehein.read


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.adapter.ReadNameAdapter
import com.aungmoehein.moehein.viewmodel.ReadViewModel
import kotlinx.android.synthetic.main.fragment_buy_book.*
import kotlinx.android.synthetic.main.fragment_read_name.*

/**
 * A simple [Fragment] subclass.
 */
class ReadNameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(ReadViewModel::class.java)
        val readAdapter = ReadNameAdapter(context!!)

        read_name_recycler.adapter = readAdapter
        read_name_recycler.layoutManager = LinearLayoutManager(context)
        viewModel.allread.observe(viewLifecycleOwner, Observer { read ->
            read?.let { readAdapter.setRead(it)} })
    }


}
