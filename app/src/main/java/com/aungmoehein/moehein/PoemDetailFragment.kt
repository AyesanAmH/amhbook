package com.aungmoehein.moehein


import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.aungmoehein.moehein.adapter.PoemListAdapter
import com.aungmoehein.moehein.viewmodel.PoemViewModel
import com.example.poemroomone.db.K5L
import com.example.poemroomone.db.Poem
import kotlinx.android.synthetic.main.fragment_poem_detail.*
import kotlinx.coroutines.*
import me.myatminsoe.mdetect.MDetect


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 *
 */
class PoemDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poem_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ptitle = PoemDetailFragmentArgs.fromBundle(arguments!!).poemtitle
        val pcontext = PoemDetailFragmentArgs.fromBundle(arguments!!).poemcontext
        val pwriter = PoemDetailFragmentArgs.fromBundle(arguments!!).poemWriter
        poem_detail_title.text = MDetect.getText(ptitle)
        poem_detail_context.text = MDetect.getText(pcontext)
        poem_detail_writer.text = MDetect.getText(pwriter)
        i("tes",randomnumber.toString())
        deletebutton.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }
    }
    val randomnumber = (1..12).shuffled().first()


}
