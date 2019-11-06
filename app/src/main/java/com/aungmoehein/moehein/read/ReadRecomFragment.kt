package com.aungmoehein.moehein.read


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.adapter.ReadRecomAdapter
import com.aungmoehein.moehein.adapter.ReadWriterAdapter
import com.aungmoehein.moehein.viewmodel.ReadViewModel
import kotlinx.android.synthetic.main.fragment_read_recom.*

/**
 * A simple [Fragment] subclass.
 */
class ReadRecomFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_recom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(ReadViewModel::class.java)
        val readRecomAdapter = ReadRecomAdapter(context!!)
        read_recom_recycler.adapter = readRecomAdapter
        read_recom_recycler.layoutManager = LinearLayoutManager(context)

        viewModel.allread.observe(this, Observer { allrecom ->
            allrecom?.let { readRecomAdapter.setReadWriterBooks(it) }
        })

        viewModel.allreadrecom.observe(this, Observer { allrebooks ->
            allrebooks?.let { readRecomAdapter.setRecomWriter(it) }
        })
    }


}
