package com.aungmoehein.moehein.read


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aungmoehein.moehein.R
import kotlinx.android.synthetic.main.fragment_read_details.*
import me.myatminsoe.mdetect.MDetect

/**
 * A simple [Fragment] subclass.
 */
class ReadDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rtitle = ReadDetailsFragmentArgs.fromBundle(arguments!!).title
        val rwriter = ReadDetailsFragmentArgs.fromBundle(arguments!!).writer
        val rrecom = ReadDetailsFragmentArgs.fromBundle(arguments!!).recom
        val rcomment = ReadDetailsFragmentArgs.fromBundle(arguments!!).comment

        read_detail_title.text = MDetect.getText(rtitle)
        read_detail_writer.text = MDetect.getText(rwriter)
        read_detail_recom.text = MDetect.getText(rrecom)
        read_detail_comment.text = MDetect.getText(rcomment)


    }


}
