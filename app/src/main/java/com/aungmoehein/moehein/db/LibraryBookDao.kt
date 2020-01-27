package com.aungmoehein.moehein.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LibraryBookDao {

    @Query("select * from library_book")
    fun getAllBooks() : LiveData<List<LibraryBook>>

    @Insert
    fun insertBook(libraryBook: LibraryBook)

    @Update
    fun updateBook(libraryBook: LibraryBook)

    @Delete
    fun deleteBook(libraryBook: LibraryBook)

    @Query("select cat from library_book group by cat order by count(cat) desc")
    fun getSugCat() : Array<String>

    @Query("select * from library_book where name = :name")
    fun checkConflict(name:String) : LibraryBook
}