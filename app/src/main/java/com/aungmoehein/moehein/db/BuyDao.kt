package com.aungmoehein.moehein.db

import androidx.appcompat.widget.DialogTitle
import androidx.lifecycle.LiveData
import androidx.room.*
import org.w3c.dom.Comment

@Dao
interface BuyDao {

    // get all buy
    @Query("select * from buy_table  order by title asc")
    fun getAllBuy():LiveData<List<Buy>>

    //get all buy writer
    @Query("select * from buy_table group by writer ORDER BY writer asc")
    fun getAllBuyWriter():LiveData<List<Buy>>

    @Query ("select * from buy_table order by title asc  ")
    fun getAllWriterBooks():LiveData<List<Buy>>

    //insert buy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuy(buy: Buy)

    //select buy by id
    @Query("select * from buy_table where id = :id")
    fun getBuyById(id:Long):Buy

    @Delete
    fun deleteBuy(buy: Buy)

    @Update
    fun updateBuy(buy: Buy)

    //check conflict
    @Query("select * from buy_table where title = :title and writer = :writer and quantity = :quantity and comment = :comment")
    fun checkBuyConflict(title:String,writer:String,quantity:Long,comment: String):Buy


    //testing
    @Query("select writer  from buy_table group by writer")
    fun test (): LiveData<List<String>>


}