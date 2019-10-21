package com.aungmoehein.moehein.poem


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.viewmodel.PoemViewModel
import com.aungmoehein.moehein.db.Poem
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.android.synthetic.main.fragment_poem_add.cancel_poem
import kotlinx.android.synthetic.main.fragment_poem_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit

/**
 * A simple [Fragment] subclass.
 */
class  PoemEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poem_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ZawNi
        fun roomText(text:String): String {
            if(MDetect.isUnicode()){
                return text
            }
            return Rabbit.zg2uni(text)
        }


        //set hint
        edit_poem_title.hint = MDetect.getText("အမည်")
        edit_poem_context.hint = MDetect.getText("ကဗျာ/စကားစု")
        edit_poem_writer.hint = MDetect.getText("စာရေးသူ")


        //Set Button Text
        val pid = PoemEditFragmentArgs.fromBundle(arguments!!).id
        val ptitle = PoemEditFragmentArgs.fromBundle(arguments!!).title
        val pcontext = PoemEditFragmentArgs.fromBundle(arguments!!).context
        val pwriter = PoemEditFragmentArgs.fromBundle(arguments!!).writer
        edit_poem_title.setText(MDetect.getText(ptitle))
        edit_poem_context.setText(MDetect.getText(pcontext))
        edit_poem_writer.setText(MDetect.getText(pwriter))
        edit_poem.text = MDetect.getText("ပြင်မည်")
        cancel_poem.text = MDetect.getText("မပြင်တော့ပါ")

        val viewModel = ViewModelProviders.of(this).get(PoemViewModel::class.java)


        //edit poem
        edit_poem.setOnClickListener {
            val add_title = roomText(edit_poem_title.text.toString())
            val add_context = roomText(edit_poem_context.text.toString())
            val add_writer = roomText(edit_poem_writer.text.toString())
            if(add_title.equals("")){
                edit_poem_title.hint = MDetect.getText("ခေါင်းစဉ်ရေးပါ")
            }
            else if (add_context.equals(""))
                edit_poem_context.hint = MDetect.getText("ကဗျာ/စကားစုကိုရေးပါ")
            else if(add_writer.equals(""))
                edit_poem_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ")
            else{
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    db.poemDao().updatePoem(Poem(id = pid,title = add_title ,context = add_context,writer = add_writer ))
                }
//                viewModel.insertPoem(Poem(title = add_title ,context = add_context,writer = add_writer ))
                activity!!.onBackPressed()
            }

        }

        //cancel poem
        cancel_poem.setOnClickListener {
            activity!!.onBackPressed()
        }
    }


}
