package com.aungmoehein.moehein.review


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.adapter.ReviewTopAdapter
import com.aungmoehein.moehein.viewmodel.ReviewViewModel
import kotlinx.android.synthetic.main.fragment_review_latest.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewLatestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_latest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(ReviewViewModel::class.java)
        val reviewAdapter = ReviewTopAdapter(context!!)
        review_latest_recycler.adapter = reviewAdapter
        review_latest_recycler.layoutManager = GridLayoutManager(context,3)
        viewModel.allreviews.observe(viewLifecycleOwner, Observer { reveiws ->
            reveiws.let { reviewAdapter.setReview(it) }
        })

    }

}
