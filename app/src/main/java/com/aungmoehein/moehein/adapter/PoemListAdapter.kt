package com.aungmoehein.moehein.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.poem.PoemListFragmentDirections
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Poem
import kotlinx.android.synthetic.main.pop_up_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect

class PoemListAdapter(context: Context):RecyclerView.Adapter<PoemListAdapter.PoemViewHolder>(){
    private val layoutInflater = LayoutInflater.from(context)
    private var poems = emptyList<Poem>()
    val db = MoeHein.getInstance(context)
    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoemListAdapter.PoemViewHolder {
        return PoemViewHolder(layoutInflater.inflate(R.layout.poem_list_layout,parent,false),this)
    }

    override fun getItemCount(): Int {
        return poems.size
    }

    override fun onBindViewHolder(holder: PoemViewHolder, position: Int) {
        holder.title.text=MDetect.getText(poems[position].title)
        holder.context.text = MDetect.getText(poems[position].context)


        holder.pop_up_button.setOnClickListener {
            val dialog = LayoutInflater.from(context).inflate(R.layout.pop_up_layout,null)
            val builder = AlertDialog.Builder(context)
                .setView(dialog)
            val alertDialog = builder.show()
            dialog.pop_up_delete.setOnClickListener {
                alertDialog.dismiss()
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                db.poemDao().deletePoem(poems[position])
                }
            }


            dialog.pop_up_edit.setOnClickListener {
                val poemEdit = PoemListFragmentDirections.poemEditAction(poems[position].id,poems[position].title,poems[position].context,poems[position].writer)
                Navigation.findNavController(holder.pop_up_button).navigate(poemEdit)
                alertDialog.dismiss()

            }
        }
    }



    internal fun setPoems(poems: List<Poem>){
        this.poems = poems
        notifyDataSetChanged()
    }




    class PoemViewHolder(itemView:View,adapter: PoemListAdapter):RecyclerView.ViewHolder(itemView),View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }
        val title = itemView.findViewById<TextView>(R.id.title)
        val context = itemView.findViewById<TextView>(R.id.context)
        val pop_up_button = itemView.findViewById<ImageButton>(R.id.pop_up_btn)
        private val poemListAdapter = adapter

        override fun onClick(v: View?) {
            val poem_details = PoemListFragmentDirections
                .poemDetailAction(poemListAdapter.poems[adapterPosition].title,
                    poemListAdapter.poems[adapterPosition].context,
                    poemListAdapter.poems[adapterPosition].writer)

            Navigation.findNavController(itemView).navigate(poem_details)
        }


    }

}