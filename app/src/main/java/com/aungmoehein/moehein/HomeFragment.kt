package com.aungmoehein.moehein


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.button_book.*
import kotlinx.android.synthetic.main.button_lovepresent.*
import kotlinx.android.synthetic.main.button_music.*
import kotlinx.android.synthetic.main.button_poem.*
import kotlinx.android.synthetic.main.button_review.*
import kotlinx.android.synthetic.main.buybutton.*
import kotlinx.android.synthetic.main.poem_top_view.*
import me.myatminsoe.mdetect.MDetect


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setText()
        poem_btn.setOnClickListener {
            val poemList = HomeFragmentDirections.poemListAction()
            Navigation.findNavController(it).navigate(poemList)
        }
    }



    fun setText(){
        poem_title.text = MDetect.getText(poem_title.text.toString())
        poem_content.text = MDetect.getText(poem_content.text.toString())
        poem_detail.text = MDetect.getText(poem_detail.text.toString())
        poem_btn_text.text = MDetect.getText("ကဗျာ၊စာစု")
        love_btn_text.text = MDetect.getText("သို့")
        buy_btn_text.text = MDetect.getText("ဝယ်မည်")
        music_btn_text.text = MDetect.getText("သောတ")
        review_btn_text.text = MDetect.getText("သုံးသပ်ချက်")
        book_btn_text.text = MDetect.getText("ဖတ်မည်")
    }

}
