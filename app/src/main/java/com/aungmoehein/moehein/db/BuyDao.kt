package com.aungmoehein.moehein.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BuyDao {

    // get all buy
    @Query("select * from buy_table  order by id desc")
    fun getAllBuy():LiveData<List<Buy>>

    //get all buy writer
    @Query("select * from buy_table ORDER BY title asc")
    fun getAllBuyWriter():LiveData<List<Buy>>

    //insert buy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBuy(buy: Buy)

    //select buy by id
    @Query("select * from buy_table where id = :id")
    fun getBuyById(id:Long):Buy

    @Delete
    fun deleteBuy(buy: Buy)

    @Update
    fun updateBuy(buy: Buy)

}