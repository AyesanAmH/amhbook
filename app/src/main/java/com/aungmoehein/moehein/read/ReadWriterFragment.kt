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
import com.aungmoehein.moehein.adapter.ReadWriterAdapter
import com.aungmoehein.moehein.viewmodel.ReadViewModel
import kotlinx.android.synthetic.main.fragment_read_writer.*

/**
 * A simple [Fragment] subclass.
 */
class ReadWriterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_writer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(ReadViewModel::class.java)
        val readWriterAdapter = ReadWriterAdapter(context!!)
        read_recycler.adapter = readWriterAdapter
        read_recycler.layoutManager = LinearLayoutManager(context)

        viewModel.allread.observe(this, Observer { read ->
            read?.let { readWriterAdapter.setReadWriter(it) }
        })

       viewModel.allreadwriter.observe(this, Observer { readwriter ->
           readwriter?.let { readWriterAdapter.setReadWriterBooks(it) }
       })

    }


}
