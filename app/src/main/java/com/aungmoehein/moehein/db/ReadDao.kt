package com.aungmoehein.moehein.db

import android.widget.ArrayAdapter
import androidx.appcompat.widget.DialogTitle
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReadDao {

    @Query("select * from read_table order by title asc")
    fun getAllRead():LiveData<List<Read>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRead(read: Read)

    @Update
    fun updateRead(read: Read)

    @Delete
    fun deleteRead(read: Read)

    @Query("select * from read_table where title = :title")
    fun checkConflict(title: String):Read

    @Query("select * from read_table group by writer order by writer asc")
    fun getAllReadWriter():LiveData<List<Read>>

    @Query("select * from read_table group by recom order by recom asc")
    fun getAllReadRecom():LiveData<List<Read>>

    @Query("select writer from read_table group by writer order by count(writer) desc")
    fun getSugWriter():Array<String>

    @Query("select recom from read_table group by recom order by count(recom) desc")
    fun getSugRecom():Array<String>
}