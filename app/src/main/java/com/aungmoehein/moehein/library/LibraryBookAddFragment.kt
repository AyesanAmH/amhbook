package com.aungmoehein.moehein.library


import android.graphics.Color
import android.os.Bundle
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.Utils.roomText
import com.aungmoehein.moehein.db.LibraryBook
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.android.synthetic.main.fragment_library_book_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import org.jetbrains.anko.hintTextColor

/**
 * A simple [Fragment] subclass.
 */
class LibraryBookAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_book_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = LibraryBookAddFragmentArgs.fromBundle(arguments!!)
        library_add_writer.text = MDetect.getText(args.name)


        library_add_name.hint = MDetect.getText("စာအုပ်အမည်")
        library_add_cat.hint = MDetect.getText("စာအုပ်အမျိုးအစား")
        cancel_book_add.text = MDetect.getText("မသိမ်းတော့ပါ")
        save_book_add.text = MDetect.getText("သိမ်းမည်")

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val db = MoeHein.getInstance(context!!)
            val cat = MDetect.getStringArray(db.libraryBookDao().getSugCat())

            async(Dispatchers.Main){
                val catAdapter = ArrayAdapter<String>(context!!,
                    android.R.layout.simple_dropdown_item_1line,cat)
                library_add_cat.setAdapter(catAdapter)
                library_add_cat.threshold = 1

                library_add_cat.onFocusChangeListener = View.OnFocusChangeListener {
                        v, hasFocus -> if(hasFocus) library_add_cat.showDropDown() }
            }
        }

        save_book_add.setOnClickListener {
            val scope = CoroutineScope(Dispatchers.IO)
            val name = roomText(library_add_name.text.toString())
            val cat = roomText(library_add_cat.text.toString())
            if (name.isBlank()){
                library_add_name.hint = MDetect.getText("စာအုပ်အမည်ရေးပါ")
                library_add_name.hintTextColor = Color.RED
            }else if(cat.isBlank()){
                library_add_cat.hint = MDetect.getText("စာအုပ်အမျိုးအစားရေးပါ")
                library_add_cat.hintTextColor = Color.RED
            }
            else{
                val book = LibraryBook(name = roomText(name),writer = args.name,cat = roomText(cat))
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val conflict = db.libraryBookDao().checkConflict(roomText(name))
                    if(conflict == null)
                    db.libraryBookDao().insertBook(book)
                }
                activity!!.onBackPressed()
            }
        }

        cancel_book_add.setOnClickListener {
            activity!!.onBackPressed()
        }
    }


}
