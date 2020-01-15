package com.aungmoehein.moehein.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EventDao {
    @Query("select * from event_table")
    fun getAllEvent():LiveData<List<Event>>

    @Insert
    fun insertEvent(event: Event)

    @Delete
    fun deleteEvent(event: Event)

    @Update
    fun updateEvent(event: Event)

}