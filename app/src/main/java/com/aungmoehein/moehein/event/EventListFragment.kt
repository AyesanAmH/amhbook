package com.aungmoehein.moehein.event


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.adapter.EventListAdapter
import com.aungmoehein.moehein.viewmodel.EventViewModel
import kotlinx.android.synthetic.main.fragment_event_list.*
import org.jetbrains.anko.support.v4.act
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class EventListFragment : Fragment() {
    lateinit var dialog:View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(EventViewModel::class.java)
        val eventAdapter = EventListAdapter(context!!)

        recycler.adapter = eventAdapter
        recycler.layoutManager = LinearLayoutManager(context)
        viewModel.allevents.observe(viewLifecycleOwner, androidx.lifecycle.Observer { events ->
            events?.let {
                eventAdapter.setEvent(it) } })


        fab.setOnClickListener {
            val action = EventListFragmentDirections.addAction()
            Navigation.findNavController(it).navigate(action)
        }


    }


}
