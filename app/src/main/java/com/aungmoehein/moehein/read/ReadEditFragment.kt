package com.aungmoehein.moehein.read


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Read
import kotlinx.android.synthetic.main.fragment_read_add.*
import kotlinx.android.synthetic.main.fragment_read_details.*
import kotlinx.android.synthetic.main.fragment_read_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit

/**
 * A simple [Fragment] subclass.
 */
class ReadEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fun roomText(text:String): String {
            if(MDetect.isUnicode()){
                return text
            }
            return Rabbit.zg2uni(text)
        }


        read_edit_title.hint = MDetect.getText("အမည်")
        read_edit_writer.hint = MDetect.getText("စာရေးသူ")
        read_edit_recom.hint = MDetect.getText("အကြံပြုသူ")
        read_edit_comment.hint = MDetect.getText("မှတ်ချက်")

        save_edit_read.text = MDetect.getText("ပြင်မည်")
        cancel_edit_read.text = MDetect.getText("မပြင်တော့ပါ")

        val eid = ReadEditFragmentArgs.fromBundle(arguments!!).id
        val etitle = ReadEditFragmentArgs.fromBundle(arguments!!).title
        val ewriter = ReadEditFragmentArgs.fromBundle(arguments!!).writer
        val erecom = ReadEditFragmentArgs.fromBundle(arguments!!).recom
        val ecomment = ReadEditFragmentArgs.fromBundle(arguments!!).comment

        read_edit_title.setText(MDetect.getText(etitle))
        read_edit_writer.setText(MDetect.getText(ewriter))
        read_edit_recom.setText(MDetect.getText(erecom))
        read_edit_comment.setText(MDetect.getText(ecomment))

        save_edit_read.setOnClickListener{
            val etitle = roomText(read_edit_title.text.toString())
            val ewriter = roomText(read_edit_writer.text.toString())
            val erecom = roomText(read_edit_recom.text.toString())
            val ecomment = roomText(read_edit_comment.text.toString())


            if(etitle == "")
                read_title.hint = MDetect.getText("စာအုပ်အမည်ရေးပါ")
            else if (ewriter == "")
                read_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ")
            else{
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val read = Read(id = eid,title = etitle,writer = ewriter,recom = erecom,comment = ecomment)
                    val checkConflict = db.readDao().checkConflict(etitle,ewriter,erecom)
                    if(checkConflict == null)
                        db.readDao().updateRead(read)
                    else
                        db.readDao().deleteRead(read)

                }
                activity!!.onBackPressed()
            }
        }

        cancel_edit_read.setOnClickListener{
            activity!!.onBackPressed()
        }
    }


}
