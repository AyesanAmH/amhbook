package com.aungmoehein.moehein.library


import android.graphics.Color
import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_library_book_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import org.jetbrains.anko.hintTextColor
import org.jetbrains.anko.support.v4.act

/**
 * A simple [Fragment] subclass.
 */
class LibraryBookEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_book_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = LibraryBookEditFragmentArgs.fromBundle(arguments!!)
        library_edit_name.setText(MDetect.getText(args.name))
        library_edit_writer.text = MDetect.getText(args.writer)
        library_edit_cat.setText(MDetect.getText(args.cat))

        save_book_edit.text = MDetect.getText("ပြင်မည်")
        cancel_book_edit.text = MDetect.getText("မပြင်တော့ပါ")
        library_edit_name.hint = MDetect.getText("စာအုပ်အမည်")
        library_edit_cat.hint = MDetect.getText("စာအုပ်အမျိုးအစား")


        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val db = MoeHein.getInstance(context!!)
            val cat = MDetect.getStringArray(db.libraryBookDao().getSugCat())

            async(Dispatchers.Main){
                val catAdapter = ArrayAdapter<String>(context!!,
                    android.R.layout.simple_dropdown_item_1line,cat)
                library_edit_cat.setAdapter(catAdapter)
                library_edit_cat.threshold = 1

                library_edit_cat.onFocusChangeListener = View.OnFocusChangeListener {
                        v, hasFocus -> if(hasFocus) library_edit_cat.showDropDown() }
            }
        }

        save_book_edit.setOnClickListener {
            val name = roomText(library_edit_name.text.toString())
            val cat = roomText(library_edit_cat.text.toString())

            if(name.isBlank()){
                library_edit_name.hint = MDetect.getText("စာအုပ်အမည်ရေးပါ")
                library_edit_name.hintTextColor = Color.RED
            }else if(cat.isBlank()){
                library_edit_cat.hint = MDetect.getText("စာအုပ်အမျိုးအစားရေးပါ")
                library_edit_cat.hintTextColor = Color.RED
            }else{
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context!!)
                    val book = LibraryBook(id = args.id , name = name , writer = args.writer,cat = cat)
                    db.libraryBookDao().updateBook(book)
                }
                activity!!.onBackPressed()
            }
        }

        cancel_book_edit.setOnClickListener {
            activity!!.onBackPressed()
        }
    }
}
