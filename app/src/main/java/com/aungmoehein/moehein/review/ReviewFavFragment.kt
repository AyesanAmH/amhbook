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
import com.aungmoehein.moehein.adapter.ReviewFavAdapter
import com.aungmoehein.moehein.viewmodel.ReviewViewModel
import kotlinx.android.synthetic.main.fragment_review_fav.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewFavFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_fav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(ReviewViewModel::class.java)
        val reviewAdapter = ReviewFavAdapter(context!!)

        review_fav_recycler.adapter = reviewAdapter
        review_fav_recycler.layoutManager = LinearLayoutManager(context)
        viewModel.allfav.observe(viewLifecycleOwner, Observer { fav ->
            fav?.let { reviewAdapter.setFav(it) }
        })
    }

}
