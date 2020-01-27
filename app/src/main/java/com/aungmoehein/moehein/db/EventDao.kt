package com.aungmoehein.moehein.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EventDao {

    @Query("select * from event_table  order by day")
    fun getAllEvent() : LiveData<List<Event>>

    @Query("select * from event_table where day < :time order by day desc")
    fun getExpireEvent(time: Long) : LiveData<List<Event>>

    @Insert
    fun insertEvent(event: Event)

    @Delete
    fun deleteEvent(event: Event)

    @Update
    fun updateEvent(event: Event)

    @Query("select max(id) from event_table")
    fun getMaxId() : Int

    @Query("select * from event_table where id = :id")
    fun getCurrentEvent(id : Int) : Event

    @Query("select name from event_table group by name order by count(name) desc")
    fun getSugName() : Array<String>

    @Query("select place from event_table group by place order by count(place) desc")
    fun getSugPlace() : Array<String>



}