package com.aungmoehein.moehein.poem


import android.app.AlertDialog
import android.app.AlertDialog.*
import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.adapter.PoemListAdapter
import com.aungmoehein.moehein.viewmodel.PoemViewModel
import kotlinx.android.synthetic.main.fragment_poem_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PoemListFragment : Fragment() {

    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poem_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        //recycler
        val viewModel = ViewModelProviders.of(this).get(PoemViewModel::class.java)
        val poemListAdapter = PoemListAdapter(context!!)
        recycler.adapter = poemListAdapter
        recycler.layoutManager =LinearLayoutManager(context!!)
        viewModel.allPoems.observe(this, Observer { poems ->
            poems?.let { poemListAdapter.setPoems(it) }
        })


        //floating button
        fab.setOnClickListener {
            val poemAdd = PoemListFragmentDirections.poemAddAction()
            Navigation.findNavController(it).navigate(poemAdd)
        }


    }


}
