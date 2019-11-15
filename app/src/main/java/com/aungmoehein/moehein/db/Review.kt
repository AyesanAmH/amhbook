package com.aungmoehein.moehein.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review_table")
data class Review(
    @PrimaryKey(autoGenerate = true)val id:Long=0,
    @ColumnInfo(name = "name")val title:String,
    @ColumnInfo(name = "writer")val writer:String,
    @ColumnInfo(name = "cat")val cat:String,
    @ColumnInfo(name = "review") val review:String,
    @ColumnInfo(name = "favourite")val fav:Boolean
    )