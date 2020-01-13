package com.aungmoehein.moehein.poem


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aungmoehein.moehein.poem.PoemDetailFragmentArgs
import com.aungmoehein.moehein.R
import kotlinx.android.synthetic.main.fragment_poem_detail.*
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

        val arg = PoemDetailFragmentArgs.fromBundle(arguments!!)
        poem_detail_title.text = MDetect.getText(arg.poemtitle)
        poem_detail_context.text = MDetect.getText(arg.poemcontext)
        poem_detail_writer.text = MDetect.getText(arg.poemWriter)

    }


}
