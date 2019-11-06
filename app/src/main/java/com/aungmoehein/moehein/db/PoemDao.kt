package com.aungmoehein.moehein.db

import androidx.appcompat.widget.DialogTitle
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PoemDao {

    //select all poem
    @Query("select * from poem_table order by id desc")
    fun getAllPoems(): LiveData<List<Poem>>

    //insert poem
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPoem(poem: Poem)

    //select poem by id
    @Query("select * from poem_table where id = :id")
    fun getPoemById(id:Long):Poem

    //select all id
    @Query("select id from poem_table")
     fun getAllId():List<Long>

    @Delete
    fun deletePoem(poem: Poem)

    @Update
    fun updatePoem(poem:Poem)

    //check conflict
    @Query("select * from poem_table where title = :title and context = :context and writer = :writer")
    fun checkPoemConflict(title: String,context :String,writer :String):Poem


}