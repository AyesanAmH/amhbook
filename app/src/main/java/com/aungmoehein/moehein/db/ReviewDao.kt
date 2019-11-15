package com.aungmoehein.moehein.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReviewDao {

    @Query("select * from review_table order by id desc")
    fun getAllReviews() : LiveData<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(review: Review)

    @Update
    fun updateReview(review: Review)

    @Delete
    fun deleteReview(review: Review)

    @Query("select * from review_table where name = :name")
    fun checkConflict(name:String):Review

    @Query("select count(name) from review_table")
    fun countName():Long

    @Query("select writer from review_table group by writer")
    fun countWriter():List<String>

    @Query("select cat from review_table group by cat")
    fun countCat():List<String>

    @Query("select count(favourite) from review_table where favourite = :fav")
    fun countFav(fav:Boolean):Long

    @Query("select cat from review_table group by cat order by count(cat) desc")
    fun getAllCat():Array<String>

    @Query("select writer from review_table group by writer order by count(writer) desc")
    fun getSugWriter():Array<String>



}
