package com.aungmoehein.moehein.read


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.Utils.roomText
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Read
import kotlinx.android.synthetic.main.fragment_read_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import org.jetbrains.anko.hintTextColor

/**
 * A simple [Fragment] subclass.
 */
class ReadAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val db = MoeHein.getInstance(context!!)
            val writer = MDetect.getStringArray(db.readDao().getSugWriter())
            val recom = MDetect.getStringArray(db.readDao().getSugRecom())


            async (Dispatchers.Main){
                val writerAdapter = ArrayAdapter<String>(context!!,
                    android.R.layout.simple_dropdown_item_1line,writer)
                val recomAdapter = ArrayAdapter<String>(context!!,
                    android.R.layout.simple_dropdown_item_1line,recom)

                read_writer.setAdapter(writerAdapter)
                read_writer.threshold = 1
                read_recom.setAdapter(recomAdapter)
                read_recom.threshold = 1


                read_writer.onFocusChangeListener = View.OnFocusChangeListener{
                        view,b -> if(b) read_writer.showDropDown() }
                read_recom.onFocusChangeListener = View.OnFocusChangeListener{
                        view,b -> if(b) read_recom.showDropDown() }
            }
        }


        read_title.hint = MDetect.getText("စာအုပ်အမည်")
        read_writer.hint = MDetect.getText("စာရေးသူ")
        read_recom.hint = MDetect.getText("အကြံပြုသူ")
        read_comment.hint = MDetect.getText("မှတ်ချက်")
        save_read.text = MDetect.getText("သိမ်းမည်")
        cancel_read.text = MDetect.getText("မသိမ်းတော့ပါ")


        save_read.setOnClickListener {
            val add_title = roomText(read_title.text.toString())
            val add_writer = roomText(read_writer.text.toString())
            val add_recom = roomText(read_recom.text.toString())
            val add_comment = roomText(read_comment.text.toString())

            if(add_title.isBlank()){
                read_title.hint = MDetect.getText("စာအုပ်အမည်ရေးပါ")
                read_title.hintTextColor = Color.RED }
            else if (add_writer.isBlank()){
                read_writer.hint = MDetect.getText("စာရေးသူအမည်ရေးပါ")
                read_writer.hintTextColor = Color.RED }
            else{
                val coroutineScope = CoroutineScope(Dispatchers.IO)
                coroutineScope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val checkConflict = db.readDao().checkConflict(add_title)
                    if(checkConflict == null)
                        db.readDao().insertRead(Read(title = add_title,writer = add_writer,recom = add_recom,comment = add_comment)) }
                activity!!.onBackPressed()
            }
        }

        cancel_read.setOnClickListener { activity!!.onBackPressed() }

    }


}
