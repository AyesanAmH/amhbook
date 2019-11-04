package com.aungmoehein.moehein.read


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Read
import kotlinx.android.synthetic.main.fragment_read.*
import kotlinx.android.synthetic.main.fragment_read_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit

/**
 * A simple [Fragment] subclass.
 */
class ReadAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fun roomText(text:String): String {
            if(MDetect.isUnicode()){
                return text
            }
            return Rabbit.zg2uni(text)
        }


        read_title.hint = MDetect.getText("အမည်")
        read_writer.hint = MDetect.getText("စာရေးသူ")
        read_recom.hint = MDetect.getText("အကြံပြုသူ")
        read_comment.hint = MDetect.getText("မှတ်ချက်")

        save_read.text = MDetect.getText("သိမ်းမည်")
        cancel_read.text = MDetect.getText("မသိမ်းတော့ပါ")



        save_read.setOnClickListener {
            val add_title = roomText(read_title.text.toString())
            val add_writer = roomText(read_writer.text.toString())
            var add_recom = roomText(read_recom.text.toString())
            val add_comment = roomText(read_comment.text.toString())


            if(add_title == "")
                read_title.hint = MDetect.getText("စာအုပ်အမည်ရေးပါ")
            else if (add_writer == "")
                read_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ")
            else{
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val checkConflict = db.readDao().checkConflict(add_title,add_writer,add_recom,add_comment)
                    if(checkConflict == null)
                        db.readDao().insertRead(Read(title = add_title,writer = add_writer,recom = add_recom,comment = add_comment))
                }
                activity!!.onBackPressed()
            }
        }

        cancel_read.setOnClickListener {
            activity!!.onBackPressed()
        }

    }


}
