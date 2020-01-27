package com.aungmoehein.moehein.library


import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.adapter.LibraryBookAdapter
import com.aungmoehein.moehein.db.LibraryBook
import com.aungmoehein.moehein.viewmodel.LibraryBookViewModel
import kotlinx.android.synthetic.main.fragment_library_writer_book.*
import me.myatminsoe.mdetect.MDetect

/**
 * A simple [Fragment] subclass.
 */
class LibraryWriterBookFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_writer_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = LibraryWriterBookFragmentArgs.fromBundle(arguments!!)
        writer_name.text = MDetect.getText(args.name)

        val viewModel = ViewModelProviders.of(this).get(LibraryBookViewModel::class.java)
        val adapter = LibraryBookAdapter(context!!,args.name)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)
        viewModel.allbooks.observe(viewLifecycleOwner, Observer { books ->
            books.forEach {
                i("writerbooks",it.toString())
            }
            books?.let { adapter.setList(it) }
        })


        fab.setOnClickListener {
            val action = LibraryWriterBookFragmentDirections.addAction(args.name)
            Navigation.findNavController(it).navigate(action)
        }
    }


}
