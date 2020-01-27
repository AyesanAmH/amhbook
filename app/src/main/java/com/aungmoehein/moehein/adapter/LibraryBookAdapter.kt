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
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.db.LibraryBook
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.library.LibraryWriterBookFragmentDirections
import kotlinx.android.synthetic.main.pop_up_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect

class LibraryBookAdapter(val context: Context,writer:String) : RecyclerView.Adapter<LibraryBookAdapter.LibraryBookHolder>() {
    var list = emptyList<LibraryBook>()
    val writer = writer
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LibraryBookAdapter.LibraryBookHolder {
        return LibraryBookHolder(LayoutInflater.from(context).inflate(R.layout.library_book_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LibraryBookAdapter.LibraryBookHolder, position: Int) {
        val position = list[position]
        holder.name.text = MDetect.getText(position.name)
        holder.cat_writer.text = MDetect.getText(position.cat)
        holder.pop_up_btn.setOnClickListener {
            val dialog = LayoutInflater.from(context).inflate(R.layout.pop_up_layout,null)
            val builder = AlertDialog.Builder(context)
                .setView(dialog)
            val alertDialog = builder.show()
            dialog.pop_up_edit.text = MDetect.getText("ပြင်မည်")
            dialog.pop_up_delete.text = MDetect.getText("ဖျက်မည်")
            dialog.pop_up_delete.setOnClickListener {
                alertDialog.dismiss()
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val db = MoeHein.getInstance(context)
                    db.libraryBookDao().deleteBook(position)
                }
            }
            dialog.pop_up_edit.setOnClickListener {
                alertDialog.dismiss()
                val action = LibraryWriterBookFragmentDirections.editAction(position.id,position.name,position.writer,position.cat)
                Navigation.findNavController(holder.pop_up_btn).navigate(action)
            }
        }

    }

    internal fun setList(list: List<LibraryBook>){
        val books = mutableListOf<LibraryBook>()
        list.forEach {
            if(it.writer.equals(this.writer))
                books.add(it)
        }
        this.list = books
        notifyDataSetChanged()
    }

    class LibraryBookHolder(itemView :View):RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.library_book_name)
        val cat_writer = itemView.findViewById<TextView>(R.id.library_cat_writer)
        val pop_up_btn = itemView.findViewById<ImageButton>(R.id.pop_up_btn)
    }
}