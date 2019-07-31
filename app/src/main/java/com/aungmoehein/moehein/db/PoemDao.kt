package com.example.poemroomone.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PoemDao {

    //select all poem
    @Query("select * from poem_table")
    fun getAllPoems(): LiveData<List<Poem>>

    //insert poem
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPoem(poem: Poem)

    //select poem by id
    @Query("select * from poem_table where id = :id")
    fun getPoemById(id:Long):List<Poem>
}