package com.aungmoehein.moehein.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LibraryWriterDao {

    @Query("select * from library_writer order by name asc")
    fun getAllWriters() : LiveData<List<LibraryWriter>>

    @Insert
    fun insertWriter(libraryWriter: LibraryWriter)

    @Delete
    fun deleteWriter(libraryWriter: LibraryWriter)

    @Update
    fun updateWriter(libraryWriter: LibraryWriter)

    @Query("select count(id) from library_writer")
    fun getWriterCount() : Int
}