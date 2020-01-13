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
import com.aungmoehein.moehein.adapter.ReviewCatAdapter
import com.aungmoehein.moehein.viewmodel.ReviewViewModel
import kotlinx.android.synthetic.main.fragment_review_cat.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewCatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_cat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(ReviewViewModel::class.java)
        val reviewAdapter = ReviewCatAdapter(context!!)

        review_cat_recycler.adapter = reviewAdapter
        review_cat_recycler.layoutManager = LinearLayoutManager(context)
        viewModel.allcats.observe(viewLifecycleOwner, Observer { cats ->
            cats?.let { reviewAdapter.setCat(it) } })
        viewModel.allnames.observe(viewLifecycleOwner, Observer { books ->
            books?.let { reviewAdapter.setBook(it) } })
    }

}
