package com.aungmoehein.moehein.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.PoemDetailFragment
import com.aungmoehein.moehein.PoemListFragmentDirections
import com.aungmoehein.moehein.R
import com.example.poemroomone.db.Poem
import me.myatminsoe.mdetect.MDetect

class PoemListAdapter(context: Context):RecyclerView.Adapter<PoemListAdapter.PoemViewHolder>(){
    private val layoutInflater = LayoutInflater.from(context)
    private var poems = emptyList<Poem>()


    class PoemViewHolder(itemView:View,adapter: PoemListAdapter):RecyclerView.ViewHolder(itemView),View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        val title = itemView.findViewById<TextView>(R.id.title)
        val context = itemView.findViewById<TextView>(R.id.context)
        private val poemListAdapter = adapter
        override fun onClick(v: View?) {
            val poem_details = PoemListFragmentDirections
                .poemDetailAction(poemListAdapter.poems[adapterPosition].title,
                    poemListAdapter.poems[adapterPosition].context,
                    poemListAdapter.poems[adapterPosition].writer)

            Navigation.findNavController(context).navigate(poem_details)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoemListAdapter.PoemViewHolder {
        return PoemViewHolder(layoutInflater.inflate(R.layout.poem_list_layout,parent,false),this)
    }

    override fun getItemCount(): Int {
        return poems.size
    }

    override fun onBindViewHolder(holder: PoemListAdapter.PoemViewHolder, position: Int) {
        holder.title.text=MDetect.getText(poems[position].title)
        holder.context.text = MDetect.getText(poems[position].context)
    }

    internal fun setPoems(poems: List<Poem>){
        this.poems = poems
        notifyDataSetChanged()
    }
}