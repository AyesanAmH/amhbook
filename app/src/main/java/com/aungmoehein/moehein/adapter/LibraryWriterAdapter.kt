package com.aungmoehein.moehein.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.R
import com.aungmoehein.moehein.Utils
import com.aungmoehein.moehein.db.LibraryBook
import com.aungmoehein.moehein.db.LibraryWriter
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.library.LibraryFragmentDirections
import kotlinx.android.synthetic.main.library_writer_rename_layout.view.*
import kotlinx.android.synthetic.main.pop_up_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.myatminsoe.mdetect.MDetect
import org.jetbrains.anko.backgroundColor

class LibraryWriterAdapter(val context: Context) : RecyclerView.Adapter<LibraryWriterAdapter.LibraryHolder>() {
    var list = emptyList<LibraryWriter>()
    var books = emptyList<LibraryBook>()
    val db = MoeHein.getInstance(context)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): LibraryWriterAdapter.LibraryHolder {
        return LibraryHolder(LayoutInflater.from(context).inflate(R.layout.library_writer_layout,parent,false),this)

    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: LibraryWriterAdapter.LibraryHolder, position: Int) {
        val writerBooks = mutableListOf<LibraryBook>()
        val scope = CoroutineScope(Dispatchers.IO)
        books.forEach {
            if(it.writer == list[position].name)
                writerBooks.add(it) }

        holder.name.text = MDetect.getText(list[position].name)
        holder.qty.text = MDetect.getText("စာအုပ် - ${Utils.myanNum(writerBooks.size.toString())} အုပ်")

        holder.pop_up_btn.backgroundColor = Color.TRANSPARENT
        holder.pop_up_btn.setOnClickListener {
            val dialog = LayoutInflater.from(context).inflate(R.layout.pop_up_layout,null)
            val builder = AlertDialog.Builder(context)
                .setView(dialog)
            val alertDialog = builder.show()
            dialog.pop_up_edit.text = MDetect.getText("ပြင်မည်")
            dialog.pop_up_delete.text = MDetect.getText("ဖျက်မည်")
            dialog.pop_up_delete.setOnClickListener {
                alertDialog.dismiss()
                scope.launch {
                    db.libraryWriterDao().deleteWriter(list[position])
                    writerBooks.forEach {
                        db.libraryBookDao().deleteBook(it)
                    }
                }

            }

            dialog.pop_up_edit.setOnClickListener {
                alertDialog.dismiss()
                val renameDialog = LayoutInflater.from(context).inflate(R.layout.library_writer_rename_layout,null)
                val renameBuilder = AlertDialog.Builder(context)
                    .setView(renameDialog)
                val renameAlertDialog = renameBuilder.show()
                renameDialog.rename_title.text = MDetect.getText("စာအုပ်စင်အမည်")
                renameDialog.rename_shelf.setText(MDetect.getText(list[position].name))
                renameDialog.rename_cancel.backgroundColor = Color.TRANSPARENT
                renameDialog.rename_save.backgroundColor = Color.TRANSPARENT
                renameDialog.rename_save.setOnClickListener {
                    scope.launch {
                        val libraryWriter = LibraryWriter(id = list[position].id,name = Utils.roomText(renameDialog.rename_shelf.text.toString()),qty = 0)
                        db.libraryWriterDao().updateWriter(libraryWriter)
                        writerBooks.forEach {
                            val book = LibraryBook(id = it.id,name = it.name,writer =  Utils.roomText(renameDialog.rename_shelf.text.toString()),cat = it.cat)
                            db.libraryBookDao().updateBook(book)
                        }
                        renameAlertDialog.dismiss()
                    }

                }

                renameDialog.rename_cancel.setOnClickListener {
                    renameAlertDialog.dismiss()

                }


            }

        }
    }

    internal fun setList(list: List<LibraryWriter>){
        this.list = list
        notifyDataSetChanged()
    }

    internal fun setBooks(books : List<LibraryBook>){
        this.books = books
        notifyDataSetChanged()
    }

    class LibraryHolder(itemView : View,val adapter: LibraryWriterAdapter ) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        init { itemView.setOnClickListener(this)}

        val name = itemView.findViewById<TextView>(R.id.shelf_name)
        val qty = itemView.findViewById<TextView>(R.id.shelf_books_qty)
        val pop_up_btn = itemView.findViewById<ImageButton>(R.id.pop_up_btn)


        override fun onClick(v: View?) {
            val action = LibraryFragmentDirections.writerBookAction(adapter.list[adapterPosition].name)
            Navigation.findNavController(itemView).navigate(action)
        }
    }

//    private fun showSoftKeyboard(view : View){
//        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
//
//    }
//    private fun closeSoftKeyboard(){
//        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0)
//    }
}