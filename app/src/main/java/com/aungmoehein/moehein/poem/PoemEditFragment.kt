package com.aungmoehein.moehein.poem


import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.Utils
import com.aungmoehein.moehein.Utils.roomText
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
import org.jetbrains.anko.support.v4.toast

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

        val arg = PoemEditFragmentArgs.fromBundle(arguments!!)

        //set hint
        edit_poem_title.hint = MDetect.getText("အမည်")
        edit_poem_context.hint = MDetect.getText("ကဗျာ/စကားစု")
        edit_poem_writer.hint = MDetect.getText("စာရေးသူ")


        //Set Button Text
        edit_poem_title.setText(MDetect.getText(arg.title))
        edit_poem_context.setText(MDetect.getText(arg.context))
        edit_poem_writer.setText(MDetect.getText(arg.writer))
        edit_poem.text = MDetect.getText("ပြင်မည်")
        cancel_poem.text = MDetect.getText("မပြင်တော့ပါ")



        //edit poem
        edit_poem.setOnClickListener {
            val add_title = Utils.roomText(edit_poem_title.text.toString())
            val add_context = roomText(edit_poem_context.text.toString())
            var formatted_add_context = ""
            add_context.lines().forEach {
                if( it.isNotBlank()){
                    if(formatted_add_context.isEmpty())
                        formatted_add_context = it
                    else
                        formatted_add_context += "\n$it"
                }
            }
            val add_writer = roomText(edit_poem_writer.text.toString())
            if(add_title.isBlank()){
                edit_poem_title.hint = MDetect.getText("ခေါင်းစဉ်ရေးပါ")
            }
            else if (add_context.isBlank())
                edit_poem_context.hint = MDetect.getText("ကဗျာ/စကားစုကိုရေးပါ")
            else if(add_writer.isBlank())
                edit_poem_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ")
            else{
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val poem = Poem(id = arg.id,title = add_title ,context = formatted_add_context,writer = add_writer)
                        db.poemDao().updatePoem(poem)
                }
                activity!!.onBackPressed() }
        }

        cancel_poem.setOnClickListener { activity!!.onBackPressed() }
    }


}
