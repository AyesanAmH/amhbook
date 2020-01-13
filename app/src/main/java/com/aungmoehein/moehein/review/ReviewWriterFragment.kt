package com.aungmoehein.moehein.review


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.adapter.ReviewWriterAdapter
import com.aungmoehein.moehein.viewmodel.ReviewViewModel
import kotlinx.android.synthetic.main.fragment_review_writer.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewWriterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_writer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(ReviewViewModel::class.java)
        val reviewAdapter = ReviewWriterAdapter(context!!)

        review_writer_recycler.adapter = reviewAdapter
        review_writer_recycler.layoutManager = LinearLayoutManager(context)
        viewModel.allwriters.observe(viewLifecycleOwner, Observer { writer ->
            writer?.let { reviewAdapter.setReveiw(it) } })
        viewModel.allnames.observe(viewLifecycleOwner, Observer {names ->
            names?.let { reviewAdapter.setReviewBook(it) }
        })
    }



}
