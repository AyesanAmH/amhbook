package com.aungmoehein.moehein.db

import androidx.appcompat.widget.DialogTitle
import androidx.lifecycle.LiveData
import androidx.room.*
import org.w3c.dom.Comment

@Dao
interface BuyDao {

    @Query("select * from buy_table order by title asc")
    fun getAllBuy():LiveData<List<Buy>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertBuy(buy: Buy)

    @Delete
    fun deleteBuy(buy: Buy)

    @Update
    fun updateBuy(buy: Buy)

    @Query("select * from buy_table where title = :title and writer = :writer and quantity = :quantity and comment = :comment")
    fun checkBuyConflict(title:String,writer:String,quantity:Long,comment: String):Buy


    @Query("select * from buy_table group by writer order by writer asc")
    fun getAllBuyWriter():LiveData<List<Buy>>




}