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
import com.aungmoehein.moehein.adapter.ReviewNameAdapter
import com.aungmoehein.moehein.viewmodel.ReviewViewModel
import kotlinx.android.synthetic.main.fragment_reveiw_name.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewNameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reveiw_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(ReviewViewModel::class.java)
        val reviewAdapter = ReviewNameAdapter(context!!)

        review_name_recycler.adapter = reviewAdapter
        review_name_recycler.layoutManager = LinearLayoutManager(context)
        viewModel.allnames.observe(viewLifecycleOwner , Observer { review ->
            review?.let { reviewAdapter.setReview(it) } })

    }


}
