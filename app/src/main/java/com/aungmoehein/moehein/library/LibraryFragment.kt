package com.aungmoehein.moehein.library


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
import com.aungmoehein.moehein.Utils
import com.aungmoehein.moehein.adapter.LibraryWriterAdapter
import com.aungmoehein.moehein.db.LibraryWriter
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.viewmodel.LibraryBookViewModel
import com.aungmoehein.moehein.viewmodel.LibraryWriterViewmodel
import kotlinx.android.synthetic.main.fragment_library.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shelf_names.setText(MDetect.getText("စာအုပ်စင်များ"))

        val adapter = LibraryWriterAdapter(context!!)
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(context,2) as RecyclerView.LayoutManager?


        val viewMoedel = ViewModelProviders.of(this).get(LibraryWriterViewmodel::class.java)
        viewMoedel.allwriters.observe(viewLifecycleOwner, Observer { shelves ->
            shelves?.let { adapter.setList(it) }
        })

        val bookViewModel = ViewModelProviders.of(this).get(LibraryBookViewModel::class.java)
        bookViewModel.allbooks.observe(viewLifecycleOwner, Observer { books ->
            books?.let { adapter.setBooks(it) }
        })



        add_shelf_btn.setOnClickListener {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val db = MoeHein.getInstance(context!!)
                val count = Utils.myanNum((db.libraryWriterDao().getWriterCount() + 1).toString())
                val name = "စာရေးသူ - $count"
                db.libraryWriterDao().insertWriter(LibraryWriter(name = name,qty = 0))
            }
        }
    }


}
