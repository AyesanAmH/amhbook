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

    @Query("select * from buy_table where title = :title")
    fun checkBuyConflict(title:String):Buy

    @Query("select * from buy_table group by writer order by writer asc")
    fun getAllBuyWriter():LiveData<List<Buy>>

    @Query("select writer from buy_table group by writer order by count(writer) desc")
    fun getSugWriter():Array<String>

    @Query("select quantity  from buy_table group by quantity order by count(quantity) desc")
    fun getSugQty():Array<Long>




}