package com.aungmoehein.moehein


import android.graphics.Color
import android.graphics.Color.rgb
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.aungmoehein.moehein.viewmodel.PoemViewModel
import com.example.poemroomone.db.K5L
import com.example.poemroomone.db.Poem
import kotlinx.android.synthetic.main.fragment_poem_add.*
import kotlinx.android.synthetic.main.fragment_poem_add.poem_title
import kotlinx.android.synthetic.main.poem_top_view.*
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit


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

        val viewModel = ViewModelProviders.of(this).get(PoemViewModel::class.java)
        val db= K5L.getInstance(context!!)



        //save poem
        save_poem.setOnClickListener {
            val add_title = roomText(poem_title.text.toString())
            val add_context = roomText(poem_context.text.toString())
            val add_writer = roomText(poem_writer.text.toString())
           if(add_title.equals("")){
               poem_title.hint = "Enter Title"
           }
            else if (add_context.equals(""))
               poem_context.hint = "Enter Context"
            else if(add_writer.equals(""))
               poem_writer.hint = "Enter Writer"
            else{
               viewModel.insertPoem(Poem(title = add_title ,context = add_context,writer = add_writer ))
               activity!!.onBackPressed()
           }

        }

        //cancel poem
        cancel_poem.setOnClickListener {
            activity!!.onBackPressed()
        }


    }

}
