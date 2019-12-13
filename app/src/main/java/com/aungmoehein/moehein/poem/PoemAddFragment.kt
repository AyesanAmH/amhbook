package com.aungmoehein.moehein.poem


import android.graphics.Color
import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.viewmodel.PoemViewModel
import com.aungmoehein.moehein.db.Poem
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.android.synthetic.main.fragment_poem_add.*
import kotlinx.android.synthetic.main.fragment_poem_add.poem_title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit
import org.jetbrains.anko.hintTextColor
import org.jetbrains.anko.support.v4.toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PoemAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poem_add, container, false)
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
        poem_title.hint = MDetect.getText("အမည်")
        poem_context.hint = MDetect.getText("ကဗျာ/စကားစု")
        poem_writer.hint = MDetect.getText("စာရေးသူ")

        //Set Button Text
        save_poem.text = MDetect.getText("သိမ်းမည်")
        cancel_poem.text = MDetect.getText("မသိမ်းတော့ပါ")


        //save poem
        save_poem.setOnClickListener {
            val add_title = roomText(poem_title.text.toString())
            val add_context = roomText(poem_context.text.toString())
            var formatted_add_context = ""
            add_context.lines().forEach {
               if( it.isNotBlank()){
                   if(formatted_add_context.isEmpty())
                       formatted_add_context = it
                   else
                       formatted_add_context += "\n$it"
               }
            }
            val add_writer = roomText(poem_writer.text.toString())
           if(add_title.isEmpty()){
               poem_title.hint = MDetect.getText("ကဗျာ၊စကားစုအမည်ကိုရေးပါ")
               poem_title.hintTextColor = Color.RED
           }
            else if (add_context.isEmpty()){
               poem_context.hint = MDetect.getText("ကဗျာ/စကားစုကိုရေးပါ")
               poem_context.hintTextColor = Color.RED
           }
            else if(add_writer.isEmpty()){
               poem_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ")
               poem_writer.hintTextColor = Color.RED
           }
            else{
               val scope = CoroutineScope(Dispatchers.IO)
               scope.launch {
                   val db = MoeHein.getInstance(context!!)
                   val poem = Poem(title = add_title ,context = formatted_add_context,writer = add_writer )
                   val checkConflict = db.poemDao().checkPoemConflict(add_title,add_writer)
                   if(checkConflict.toString().equals("0") )
                   db.poemDao().insertPoem(poem)
               }
               activity!!.onBackPressed()
           }

        }

        //cancel poem
        cancel_poem.setOnClickListener {
            activity!!.onBackPressed()
        }


    }

}
